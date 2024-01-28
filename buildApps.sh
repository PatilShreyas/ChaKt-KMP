#!/bin/bash

mkdir distributions

# Build Android App
echo "Building Android App 📱"
./gradlew :composeApp:assembleDebug
echo "Verifying Android App"
cp composeApp/build/outputs/apk/debug/composeApp-debug.apk distributions/chakt-android.apk

# Build Mac Desktop App
echo "Building Mac Desktop App 🖥️"
./gradlew :composeApp:packageDmg
echo "Verifying Mac Desktop App"
cp composeApp/build/compose/binaries/main/dmg/ChaKt-1.0.0.dmg distributions/chakt.dmg

# Build Web App
echo "Building Web App 🌎"
./gradlew :composeApp:wasmJsBrowserDistribution
mkdir distributions/chakt-wasm
cp -r composeApp/build/dist/wasmJs/productionExecutable/ distributions/chakt-web/



