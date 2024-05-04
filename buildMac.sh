#!/bin/bash

mkdir distributions

# Build Mac Desktop App
echo "Building Mac Desktop App 🖥️"
./gradlew :composeApp:packageUberJarForCurrentOS
echo "Verifying Mac Desktop App"
cp composeApp/build/compose/jars/ChaKt-macos-x64-1.1.0.jar distributions/chakt-macos-x64.jar
