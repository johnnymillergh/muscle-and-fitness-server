#!/bin/bash
############################# startup ##############################
# Author: Johnny Miller, date: 7:31 AM, Dec. 5, 2020               #
# Copyright (c) Johnny Miller                                      #
# Capability: macOS Big Sur 11.2.1 (20D74)                         #
# Purpose:                                                         #
#   1. Integrate easy and simple deployment                        #
# Download: https://www.consul.io/downloads                        #
####################################################################

# Exit immediately if a command exits with a non-zero status.
# https://stackoverflow.com/questions/19622198/what-does-set-e-mean-in-a-bash-script
set -e

clear
./consul agent -config-file="config.json"
