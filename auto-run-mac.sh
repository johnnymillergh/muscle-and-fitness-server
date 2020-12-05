#!/bin/bash
############################## auto-run ############################
# Author: Johnny Miller, date: 7:31 AM, Dec. 5, 2020               #
# Copyright (c) Johnny Miller                                      #
# Capability: macOS Big Sur 11.0.1 (20B29)                         #
# Purpose:                                                         #
# Inspiration: https://google.github.io/styleguide/shellguide.html #
####################################################################

# Exit immediately if a command exits with a non-zero status.
# https://stackoverflow.com/questions/19622198/what-does-set-e-mean-in-a-bash-script
set -e

############### Configurable Environment Variables ################
readonly mavenActiveProfile="development-local"
readonly javaParameter="-Xms256m -Xmx256m -Dspring.profiles.active=$mavenActiveProfile"
# The name of service `service-registry` is `service-registry`, also equal to the directory `service-registry`.
readonly runServices=(
#  service-registry
#  spring-boot-admin
  api-portal
)
readonly skipBuild=true

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
  logInfo "[RUN] COMMAND: $runJarCommand"
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
  logWarn "[PRE-BUILD] Current Git branch: $(gitCurrentBranch), pulling codes from Git…"
  gitPull || {
    logError "[PRE-BUILD] Git pull failed. gitPullExecutionResult: $gitPullExecutionResult" >&2
    exit 1
  }
  gitPullExecutionResult=$?
  if [ "$gitPullExecutionResult" -eq 0 ]; then
    logInfo "[PRE-BUILD] Git pull success. gitPullExecutionResult: $gitPullExecutionResult"
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
# Execute run phase.
# Arguments:
#   None.
# Returns:
#   None.
#######################################
function executeRunPhase() {
  for index in "${!runServices[@]}"; do
  logInfo "[RUN] $((index + 1)). ${runServices[$index]}"
  runJar "${runServices[$index]}"
  done
  runCommandResult=$?
  if [ "$runCommandResult" -eq 0 ]; then
    logInfo "[RUN] Run success. runCommandResult: $runCommandResult"
    logInfo "[RUN] Java Program State:"
    jps -l
  else
    logError "[DEPLOY] Deployment failed. runCommandResult: $runCommandResult" >&2
    exit 1
  fi
}

################### MAIN PROCEDURE START ###################
# Pre-build phase
executePreBuildPhase
# Build phase
executeBuildPhase
# Run phase
executeRunPhase
