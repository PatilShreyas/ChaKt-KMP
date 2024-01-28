#!/bin/bash

mkdir distributions

# Build Android App
echo "Building Android App 📱"
./gradlew :composeApp:assembleDebug
echo "Verifying Android App"
cp composeApp/build/outputs/apk/debug/composeApp-debug.apk distributions/chakt-android.apk

# Build Linux Desktop App
mkdir distributions
echo "Building Linux Desktop App 🖥"
./gradlew :composeApp:packageUberJarForCurrentOS
cp composeApp/build/compose/jars/ChaKt-linux-x64-1.0.0.jar distributions/chakt-linux-x64.jar

# Build Web App
echo "Building Web App 🌎"
./gradlew :composeApp:wasmJsBrowserDistribution
mkdir distributions/chakt-wasm
cp -r composeApp/build/dist/wasmJs/productionExecutable/ distributions/chakt-web/
