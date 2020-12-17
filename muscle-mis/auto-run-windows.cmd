@ECHO OFF
@REM ############################## auto-run ############################
@REM # Author: 钟俊, date: 12:43:04 PM, Dec. 8, 2020                     #
@REM # Copyright (c) 钟俊                                                #
@REM # Capability: Windows 10 Pro (20H2)                                #
@REM # Purpose:                                                         #
@REM #   1. Integrate easy and simple deployment,                       #
@REM #   2. Reduce memory usage of IntelliJ IDEA to run services,       #
@REM #   3. Improve the efficiency of local development.                #
@REM ####################################################################

SETLOCAL EnableDelayedExpansion
@REM UTF-8
chcp 65001

@REM #################### Configurable Environment Variables ###################
SET skipGitPull=true
SET skipMavenBuild=true
SET javaExe=C:\Users\Administrator\.sdkman\candidates\java\11.0.9.hs-adpt\bin\java.exe
SET mavenActiveProfile="development-local"
SET javaParameter=-Dfile.encoding=UTF-8 -Xms256m -Xmx256m

GOTO:MAIN

@REM ############################# Common Functions ############################

@REM Log trace.
@REM %~1 Info message
:logTrace
    ECHO %date% %time% TRACE - %~1%
EXIT /B 0

@REM Log info.
@REM %~1 Info message
:logInfo
    ECHO %date% %time%  [32mINFO - %~1%[0m
EXIT /B 0

@REM Log warn.
@REM %~1 Warn message
:logWarn
    ECHO %date% %time%  [33mWARN - %~1%[0m
EXIT /B 0

@REM Log error.
@REM %~1 Error message
:logError
    ECHO %date% %time% [31mERROR - %~1%[0m
EXIT /B 0

@REM Set terminal title.
@REM %~1 Title string
:setTerminalTitle
    TITLE %~1
EXIT /B 0

@REM ############################ Custom Functions #############################

@REM Pull the latest code of current branch from Git.
:gitPull
    git pull
    if %ERRORLEVEL% NEQ 0 (
        CALL :logError "Failed to execute command `Git pull`"
        EXIT /B %ERRORLEVEL%
    )
EXIT /B 0

@REM Execute pre-build phase.
:executePreBuildPhase
    CALL :logWarn "[PRE-BUILD] Active profile: %mavenActiveProfile%"
    FOR /F "tokens=*" %%i IN ('git rev-parse --abbrev-ref HEAD') DO SET currentBranch=%%i
    CALL :logWarn "[PRE-BUILD] Current Git branch: %currentBranch%"
    if %skipGitPull% == true (
            CALL :logWarn "[PRE-BUILD] Skipped Git pull"
    ) else (
        CALL :logInfo "[PRE-BUILD] Start to pull latest code from Git"
        CALL :gitPull
    )
    CALL :logInfo "[PRE-BUILD] Java Version Information"
    CALL %javaExe% -version
    CALL :logInfo "[PRE-BUILD] Maven Version Information"
    CALL mvn -v
    CALL :logInfo "[PRE-BUILD] Current directory:"
    dir
EXIT /B 0

@REM Execute build phase.
:executeBuildPhase
    if %skipMavenBuild% == true (
        CALL :logWarn "[PRE-BUILD] Skipped Maven build"
        EXIT /B 0
    )
    CALL :logInfo "[PRE-BUILD] Start to pull latest code from Git"
    SET currentDirectory=%cd%
    @REM Change directory to root projeject
    cd ..
    CALL mvn clean package --batch-mode --show-version -Dmaven.javadoc.skip=true -DskipTests=true
    if %ERRORLEVEL% NEQ 0 (
        CALL :logError "[BUILD] Failed to execute Maven build"
        EXIT /B %ERRORLEVEL%
    )
    CALL :logInfo "[BUILD] Maven build success"
    cd %currentDirectory%
    dir
EXIT /B 0

@REM Execute run phase.
:executeRunPhase
    CALL :logWarn "[RUN] Running service…"
    FOR /F "tokens=*" %%i IN ('dir /B/A:- target\*.jar') DO SET jarFileName=%%i
    if "%jarFileName%" == "" (
        CALL :logError "[RUN] Not found runnable JAR!"
        EXIT /B 1
    )
    CALL :logWarn "[RUN] Found JAR: %jarFileName%"
    CALL :setTerminalTitle %jarFileName%
    SET runJarCommand=%javaExe% %javaParameter% -Dspring.profiles.active=%mavenActiveProfile% -jar target\%jarFileName%
    CALL :logWarn "[RUN] Execute command: %runJarCommand%"
    CALL %runJarCommand%
EXIT /B 0

@REM ############################# MAIN Procedures #############################
:MAIN
cls
@REM Pre-build phrase (Display version, Git pull)
CALL :executePreBuildPhase
if %ERRORLEVEL% NEQ 0 (
    CALL :logError "[PRE-BUILD] Failed to execute pre-build phase"
    EXIT /B %ERRORLEVEL%
)
@REM Build phase
CALL :executeBuildPhase
if %ERRORLEVEL% NEQ 0 (
    CALL :logError "[BUILD] Failed to execute build phase"
    EXIT /B %ERRORLEVEL%
)
@REM Run phase
CALL :executeRunPhase

ENDLOCAL
