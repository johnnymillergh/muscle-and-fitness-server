#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
# https://stackoverflow.com/questions/19622198/what-does-set-e-mean-in-a-bash-script
set -e

########################## Functions Import ##########################
source ./.travis/common.sh

# Directory check
CURRENT_DIR=$(pwd)
printInfo "[INSTALL] CURRENT_DIR: $CURRENT_DIR"
printInfo "[INSTALL] List of CURRENT_DIR:"
command "ls"

printInfo "[INSTALL] Start to run Maven clean, install…"
# Run the Maven clean install
./mvnw clean install --batch-mode --show-version --quiet -DskipTests=true -Dmaven.javadoc.skip=true -Djib.to.auth.username=$DOCKER_HUB_USERNAME -Djib.to.auth.password=$DOCKER_HUB_PASSWORD
INSTALL_COMMAND_RESULT=$?
if [ "$INSTALL_COMMAND_RESULT" -eq 0 ]; then
  printInfo "[INSTALL] Installation succeed. INSTALL_COMMAND_RESULT: $INSTALL_COMMAND_RESULT"
else
  printError "[INSTALL] Installation failed. INSTALL_COMMAND_RESULT: $INSTALL_COMMAND_RESULT" >&2
  exit 1
fi
