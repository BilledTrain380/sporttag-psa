#!/bin/bash

version=$1
commitHash=$2

if [ -n "$version" ]; then
  echo "Version parameter is missing"
  exit 1
fi

if [ -n "$commitHash" ]; then
  echo "Commit hash parameter is missing"
  exit 1
fi

jpackage --name PSA-Desktop \
  --main-jar "PSA-Desktop-$version+$commitHash.jar" \
  --type dmg \
  --app-version 0 \
  --description "Sport tournament management system" \
  --dest ./build/libs \
  --vendor "BilledTrain380" \
  --icon "./icons/PSA.icns" \
  --mac-package-identifier "PSA-Desktop" \
  --license-file "LICENSE.md"
