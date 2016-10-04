#!/usr/bin/env bash

# Copies xfreerdp-executable to current folder
#
# Does not work with IntelliJ because it isn't designed
# to be run as root. Run manually instead.

# `brew install homebrew/x11/freerdp --HEAD`

sudo cp $(which xfreerdp) $(pwd)/src/main/resources/xfreerdp