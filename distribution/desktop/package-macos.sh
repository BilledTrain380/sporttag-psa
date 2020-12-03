#!/bin/bash

fullVersion=$1

if [ -n "fullVersion" ]; then
  echo "Version parameter is missing"
  exit 1
fi

jpackage --name PSA-Desktop \
  --main-jar "./build/libs/PSA-Desktop-$fullVersion.jar" \
  --type dmg \
  --app-version "$fullVersion" \
  --description "Sport tournament management system" \
  --dest ./build/libs \
  --vendor "BilledTrain380" \
  --icon "./icons/PSA.icns" \
  --mac-package-identifier "PSA-Desktop" \
  --license-file "LICENSE.md"
