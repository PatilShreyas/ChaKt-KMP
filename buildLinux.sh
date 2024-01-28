#!/bin/bash

# Build Linux Desktop App
mkdir distributions
echo "Building Linux Desktop App 🖥"
./gradlew :composeApp:packageUberJarForCurrentOS
cp composeApp/build/compose/jars/chakt_1.0.0-1_amd64.jar distributions/chakt-linux-amd64.jar