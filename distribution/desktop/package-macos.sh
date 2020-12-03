#!/bin/bash

fullVersion=$1
version=$2

echo "Version: $fullVersion"

jpackage --name PSA-Desktop \
  --input "./build/libs" \
  --main-jar "PSA-Desktop-$fullVersion.jar" \
  --type pkg \
  --app-version "$version" \
  --description "Sport tournament management system" \
  --dest ./build/macos \
  --vendor "BilledTrain380" \
  --icon "./icons/PSA.icns" \
  --mac-package-identifier "PSA-Desktop" \
  --license-file "./build/resources/LICENSE.md"
