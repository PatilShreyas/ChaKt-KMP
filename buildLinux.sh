#!/bin/bash

# Build Linux Desktop App
mkdir distributions
echo "Building Linux Desktop App 🖥"
./gradlew :composeApp:packageUberJarForCurrentOS
cp composeApp/build/compose/jars/ChaKt-linux-x64-1.0.0.jar distributions/chakt-linux-x64.jar