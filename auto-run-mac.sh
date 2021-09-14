#!/bin/bash
############################## auto-run ############################
# Author: Johnny Miller, date: 7:31 AM, Dec. 5, 2020               #
# Copyright (c) Johnny Miller                                      #
# Capability: macOS Big Sur 11.2.1 (20D74)                         #
# Purpose:                                                         #
#   1. Integrate easy and simple deployment,                       #
#   2. Reduce memory usage of IntelliJ IDEA to run services,       #
#   3. Improve the efficiency of local development,                #
# Inspiration: https://google.github.io/styleguide/shellguide.html #
####################################################################

# Exit immediately if a command exits with a non-zero status.
# https://stackoverflow.com/questions/19622198/what-does-set-e-mean-in-a-bash-script
set -e

############### Configurable Environment Variables ################
readonly mavenActiveProfile="development-local"
readonly javaParameter="-Xms256m -Xmx256m -Dfile.encoding=UTF-8 -Dspring.cloud.consul.host=localhost -Dspring.profiles.active=$mavenActiveProfile"
readonly runServices=(
  auth-center
  api-gateway
)
readonly skipGitPull=true
readonly skipBuild=false
# Available options for `startMode`: "keep-previous", "overlap"
readonly startMode="overlap"
# `waitToStopTimeout` is the timeout (unit: second) for waiting Java to stop
readonly waitToStopTimeout=5

##################### Functions Declaration #######################
# Bash tips: Colors and formatting (ANSI/VT100 Control sequences) #
# https://misc.flogisoft.com/bash/tip_colors_and_formatting       #
# Pass arguments into a function                                  #
# https://bash.cyberciti.biz/guide/Pass_arguments_into_a_function #
###################################################################

#######################################
# Get formatted date
# Arguments:
#   None
# Returns:
#   Date formatted string like "2020-12-05 08:44:11+0800".
#######################################
function now() {
  date "+%Y-%m-%d %H:%M:%S%z"
}

#######################################
# Log trace. Without any color.
# Arguments:
#   $1 input string.
# Returns:
#   Trace log.
#######################################
function logTrace() {
  printf "$(now) TRACE --- %s\n" "$1"
}

#######################################
# Log info. (GREEN)
# Arguments:
#   $1 input string.
# Returns:
#   Info log.
#######################################
function logInfo() {
  printf "$(now)  \e[32mINFO --- %s\e[0m\n" "$1"
}

#######################################
# Log warn. (YELLOW)
# Arguments:
#   $1 input string.
# Returns:
#   Warn log.
#######################################
function logWarn() {
  printf "$(now)  \e[33mWARN --- %s\e[0m\n" "$1"
}

#######################################
# Log error. (RED)
# Arguments:
#   $1 input string.
# Returns:
#   Error log.
#######################################
function logError() {
  printf "$(now) \e[31mERROR --- %s\e[0m\n" "$1"
}

#######################################
# Run JAR.
# Arguments:
#   $1 The service name (same as directory) to run.
#      e.q. The name of service `service-registry` is `service-registry`,
#           also equal to the directory `service-registry`.
# Returns:
#   None.
#######################################
function runJar() {
  jarFileRelativePath=$(find "./$1/target" -name '*.jar')
  if [ -z "$jarFileRelativePath" ]; then
    logError "[RUN] Runnable JAR is not found!"
    exit 1
  fi
  logWarn "[RUN] Found JAR: $jarFileRelativePath"
  # Run Java program in background but then can't control terminal afterwards
  runJarCommand="java $javaParameter -jar $jarFileRelativePath"
  # shellcheck disable=SC2086
  nohup $runJarCommand &
  logInfo "[RUN] Executed COMMAND: $runJarCommand"
}

#######################################
# Git current branch.
# Arguments:
#   None.
# Returns:
#   Current Git branch.
#######################################
function gitCurrentBranch() {
  git rev-parse --abbrev-ref HEAD
}

#######################################
# Git pull.
# Arguments:
#   None.
# Returns:
#   Pull from current branch.
#######################################
function gitPull() {
  git pull
}

#######################################
# Execute pre build phase.
# Arguments:
#   None.
# Returns:
#   None.
#######################################
function executePreBuildPhase() {
  logWarn "[PRE-BUILD] Active profile: $mavenActiveProfile"
  if [ "$skipGitPull" = false ]; then
    logWarn "[PRE-BUILD] Current Git branch: $(gitCurrentBranch), pulling codes from Git…"
    gitPull || {
      logError "[PRE-BUILD] Git pull failed. gitPullExecutionResult: $gitPullExecutionResult" >&2
      exit 1
    }
    gitPullExecutionResult=$?
    if [ "$gitPullExecutionResult" -eq 0 ]; then
      logInfo "[PRE-BUILD] Git pull success. gitPullExecutionResult: $gitPullExecutionResult"
    fi
  else
    logWarn "[PRE-BUILD] Current Git branch: $(gitCurrentBranch), skipped Git Pull"
  fi
  logWarn "[PRE-BUILD] CURRENT_DIR: $(pwd)"
  logWarn "[PRE-BUILD] List of CURRENT_DIR:"
  ls
}

#######################################
# Execute build phase.
# Arguments:
#   None.
# Returns:
#   None.
#######################################
function executeBuildPhase() {
  logInfo "[BUILD] Maven is staring to build"
  if [ "$skipBuild" = false ]; then
    mvn clean package --batch-mode --show-version -Dmaven.javadoc.skip=true -DskipTests=true -P $mavenActiveProfile
    buildCommandResult=$?
    # After the command that might error, instead of || exit 1...
    if [ "$buildCommandResult" -ne 0 ]; then
      # Additional error handling (messaging, etc.) here.
      logError "[BUILD] Maven build failed!"
      exit 1
    else
      logInfo "[BUILD] Maven build success!"
    fi
  else
    logWarn "[BUILD] Skipped Maven build"
  fi
}

#######################################
# Check Java program state.
# Arguments:
#   $1 Java program name string.
# Returns:
#   0 Not running;
#   1 Running.
#######################################
function checkJavaProgramState() {
  jps -l | grep -c "$1"
}

#######################################
# Kill java program.
# Arguments:
#   $1 Java program name string.
# Returns:
#   None.
#######################################
function killJavaProgram() {
  # https://www.tutorialkart.com/bash-shell-scripting/bash-split-string/
  # IFS=' ' # space is set as delimiter
  # "$(jps -l | grep "$1")" is read into an array as tokens separated by IFS
  read -ra jpsArray <<<"$(jps -l | grep "$1")"
  # Access the first element of array
  kill "${jpsArray[0]}"
  killResult=$?
  sleep $waitToStopTimeout
  logError "[KILL] Waited $waitToStopTimeout (s) to kill pid: ${jpsArray[0]}. killResult: $killResult"
}

#######################################
# Iteratively run java services. Function only for Run Phase
# Arguments:
#   None.
# Returns:
#   None.
#######################################
function iterativelyRunJavaServices() {
  for index in "${!runServices[@]}"; do
    serviceName="${runServices[$index]}"
    isRunning=$(checkJavaProgramState "$serviceName") || {
      # If exception happens when executing previous command, then set 0 to isRunning
      isRunning=0
    }
    logInfo "[RUN] ### $((index + 1)). $serviceName, startMode: $startMode, isRunning: $isRunning"
    case $startMode in
    "keep-previous")
      if [ "$isRunning" == "1" ]; then
        logError "[RUN] The service \`$serviceName\` is running already. No need to start again."
        continue
      else
        runJar "$serviceName"
      fi
      ;;
    "overlap")
      if [ "$isRunning" == "1" ]; then
        logWarn "[RUN] The service \`$serviceName\` is running already. About to kill it."
        killJavaProgram "$serviceName"
      fi
      runJar "$serviceName"
      ;;
    *)
      logError "[RUN] Failed to startup! Caused by Invalid startMode: $startMode"
      exit 1
      ;;
    esac
  done
}

#######################################
# Execute run phase.
# Arguments:
#   None.
# Returns:
#   None.
#######################################
function executeRunPhase() {
  iterativelyRunJavaServices || {
    # Capture exception
    logError "[RUN] Failed to start Java services" >&2
    exit 1
  }
  logInfo "[RUN] Run success. Command result: $?. Java Program State (jps -l):"
  jps -l
}

################### MAIN PROCEDURE START ###################
# Pre-build phase
executePreBuildPhase
# Build phase
executeBuildPhase
# Run phase
executeRunPhase
