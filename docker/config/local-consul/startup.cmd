@ECHO OFF
@REM ############################## auto-run ############################
@REM # Author: Johnny Miller, date: 2021-02-08 09:23:29.314             #
@REM # Copyright (c) Johnny Miller                                      #
@REM # Capability: Windows 10 Pro (20H2)                                #
@REM # Purpose:                                                         #
@REM #   1. Integrate easy and simple deployment                        #
@REM # Download: https://www.consul.io/downloads                        #
@REM ####################################################################

SETLOCAL EnableDelayedExpansion
@REM UTF-8
chcp 65001

cls
TITLE local-consul
CALL .\consul.exe agent -config-file="config.json"
