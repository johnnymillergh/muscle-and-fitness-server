#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
# https://stackoverflow.com/questions/19622198/what-does-set-e-mean-in-a-bash-script
set -e

########################## Functions Import ##########################
source ./.shellscript/common.sh

# Directory check
CURRENT_DIR=$(pwd)
logInfo "[INSTALL] CURRENT_DIR: $CURRENT_DIR"
logInfo "[INSTALL] List of CURRENT_DIR:"
command "ls"

logInfo "[INSTALL] Start to run Maven clean, install…"
# Run the Maven clean install
./mvnw clean install --batch-mode --show-version --quiet -Dmaven.javadoc.skip=true -Djib.to.auth.username=$DOCKER_HUB_USERNAME -Djib.to.auth.password=$DOCKER_HUB_PASSWORD
INSTALL_COMMAND_RESULT=$?
if [ "$INSTALL_COMMAND_RESULT" -eq 0 ]; then
  logInfo "[INSTALL] Installation succeed. INSTALL_COMMAND_RESULT: $INSTALL_COMMAND_RESULT"
else
  logError "[INSTALL] Installation failed. INSTALL_COMMAND_RESULT: $INSTALL_COMMAND_RESULT" >&2
  exit 1
fi
