#!/bin/bash

echo
echo
echo '## Unmount all jFRDP-Volumes before executing.'
echo
echo '## YOU MUST AGREE TO SCODE LICENSE AGREEMENT FOR THIS TO WORK'
echo
echo
sleep 2

# Set JDK-Home
jdk=$(/usr/libexec/java_home)

# Create Deployment-Package
$jdk/bin/javapackager -deploy -native dmg -Bicon=src/main/resources/icon.icns -srcfiles jFRDP.jar -appclass main.Start -name jFRDP -outdir $PWD -outfile jFRDP -v

# Move to project's root-dir
mv bundles/jFRDP-1.0.dmg jFRDP-installer.dmg

# Clean not needed files
rm jFRDP.html
rm jFRDP.jnlp