@echo off

set fullVersion=%1
set version=%2

echo "Version: %fullVersion%"

jpackage --name PSA-Desktop ^
    --win-dir-chooser ^
    --win-menu ^
    --input "./build/libs" ^
    --main-jar "PSA-Desktop-%fullVersion%.jar" ^
    --type msi ^
    --app-version %version% ^
    --description "Sport tournament management system" ^
    --dest ./build/windows ^
    --vendor "BilledTrain380" ^
    --icon "./icons/psa-logo.png" ^
    --license-file "./build/resources/LICENSE.md"
