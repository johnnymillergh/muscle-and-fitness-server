@ECHO OFF
REM ############################## auto-run ############################
REM # Author: Johnny Miller (锺俊), date: 15:11:04 PM, Feb. 3, 2022     #
REM # Copyright (c) Johnny Miller (锺俊)                                #
REM # Capability: Windows 11 Pro (21H2)                                #
REM # Purpose:                                                         #
REM #   1. Integrate easy and simple deployment,                       #
REM #   2. Reduce memory usage of IntelliJ IDEA to run services,       #
REM #   3. Improve the efficiency of local development.                #
REM ####################################################################

SETLOCAL EnableDelayedExpansion
REM UTF-8
chcp 65001

REM #################### Configurable Environment Variables ###################
SET skipGitPull=true
SET skipMavenBuild=true
SET minimalJavaMajorVersion=11
SET javaExe=C:\Users\za-zhongjun\.sdkman\candidates\java\11.0.12-open\bin\java.exe
SET springActiveProfile="development-local"
SET javaParameter=-Xms256m -Xmx256m -Dfile.encoding=UTF-8 -Dspring.cloud.consul.host=localhost -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:2345

GOTO:MAIN

REM ############################# Common Functions ############################

REM Log trace.
REM %~1 Info message
:logTrace
    ECHO %date% %time% TRACE - %~1%
EXIT /B 0

REM Log info.
REM %~1 Info message
:logInfo
    ECHO %date% %time%  [32mINFO - %~1%[0m
EXIT /B 0

REM Log warn.
REM %~1 Warn message
:logWarn
    ECHO %date% %time%  [33mWARN - %~1%[0m
EXIT /B 0

REM Log error.
REM %~1 Error message
:logError
    ECHO %date% %time% [31mERROR - %~1%[0m
EXIT /B 0

REM Set terminal title.
REM %~1 Title string
:setTerminalTitle
    TITLE %~1
EXIT /B 0

REM ############################ Custom Functions #############################

REM Check Java major version
:checkJavaMajorVersion
CALL :logWarn "Start to Check Java major version…"
%javaExe% -version 1>nul 2>nul || (
  CALL :logError "Java is not installed!"
  EXIT /B 2
)
for /f tokens^=2-6^ delims^=.-_+^" %%j in ('%javaExe% -fullversion 2^>^&1') do set "currentJavaMajorVersion=%%j"
CALL :logWarn "Got current Java major version number: %currentJavaMajorVersion%"
if %currentJavaMajorVersion% LSS %minimalJavaMajorVersion% (
  CALL :logError "Current Java version is too low, at least OpenJDK %minimalJavaMajorVersion% is needed"
  EXIT /B 1
)
CALL :logInfo "Passed Java major version checking"
EXIT /B 0

REM Pull the latest code of current branch from Git.
:gitPull
    git pull
    if %ERRORLEVEL% NEQ 0 (
        CALL :logError "Failed to execute command `Git pull`"
        EXIT /B %ERRORLEVEL%
    )
EXIT /B 0

REM Execute pre-build phase.
:executePreBuildPhase
    CALL :logWarn "[PRE-BUILD] Active profile: %springActiveProfile%"
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

REM Execute build phase.
:executeBuildPhase
    if %skipMavenBuild% == true (
        CALL :logWarn "[PRE-BUILD] Skipped Maven build"
        EXIT /B 0
    )
    CALL :logInfo "[PRE-BUILD] Start to pull latest code from Git"
    SET currentDirectory=%cd%
    REM Change directory to root projeject
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

REM Execute run phase.
:executeRunPhase
    FOR /F "tokens=* USEBACKQ" %%F IN (`dir /b /s /a:d "*-bootstrap"`) DO (SET bootstrapModulePath=%%F)
    CALL :logWarn "[RUN] Found bootstrap module, about to run service. Bootstrap module path: %bootstrapModulePath%"
    FOR /F "tokens=*" %%i IN ('dir /B/A:- %bootstrapModulePath%\target\*.jar') DO SET jarFileName=%%i
    if "%jarFileName%" == "" (
        CALL :logError "[RUN] Not found runnable JAR!"
        EXIT /B 1
    )
    CALL :logWarn "[RUN] Found JAR: %jarFileName%"
    FOR %%a IN ("%~dp0\..") DO SET "baseProjectName=%%~nxa"
    CALL :setTerminalTitle "%baseProjectName% :: %jarFileName%"
    SET runJarCommand=%javaExe% %javaParameter% -Dspring.profiles.active=%springActiveProfile% -jar %bootstrapModulePath%\target\%jarFileName%
    CALL :logWarn "[RUN] Execute command: %runJarCommand%"
    CALL %runJarCommand%
EXIT /B 0

REM ############################# MAIN Procedures #############################
:MAIN
cls

REM Check Java major version
CALL :checkJavaMajorVersion
if %ERRORLEVEL% NEQ 0 (
    CALL :logError "Failed to check Java major version!"
    EXIT /B %ERRORLEVEL%
)

REM Pre-build phrase (Display version, Git pull)
CALL :executePreBuildPhase
if %ERRORLEVEL% NEQ 0 (
    CALL :logError "[PRE-BUILD] Failed to execute pre-build phase"
    EXIT /B %ERRORLEVEL%
)
REM Build phase
CALL :executeBuildPhase
if %ERRORLEVEL% NEQ 0 (
    CALL :logError "[BUILD] Failed to execute build phase"
    EXIT /B %ERRORLEVEL%
)
REM Run phase
CALL :executeRunPhase

ENDLOCAL
