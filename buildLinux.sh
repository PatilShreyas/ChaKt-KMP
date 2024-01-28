#!/bin/bash

# Build Linux Desktop App
mkdir distributions
echo "Building Linux Desktop App 🖥"
./gradlew :composeApp:packageDeb
cp composeApp/build/compose/binaries/main/deb/ChaKt-1.0.0.deb distributions/chakt.deb