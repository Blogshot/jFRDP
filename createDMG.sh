#!/bin/bash

jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager -deploy -native dmg -Bicon=src/main/resources/icon.icns -srcfiles jFRDP.jar -appclass main.Start -name jFRDP -outdir $PWD -outfile jFRDP -v
mv bundles/jFRDP-1.0.dmg jFRDP-installer.dmg