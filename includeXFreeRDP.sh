#!/usr/bin/env bash

# Copies xfreerdp-executable to current folder
#
# Does not work with IntelliJ because it isn't designed
# to be run as root. Run manually instead.
sudo cp $(which xfreerdp) $(pwd)/src/main/resources/xfreerdp