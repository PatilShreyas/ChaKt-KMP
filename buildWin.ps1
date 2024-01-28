New-Item -Path "distributions" -ItemType Directory
.\gradlew :composeApp:packageUberJarForCurrentOS
Copy-Item -Path composeApp/build/compose/jars/ChaKt-windows-x64-1.0.0.jar distributions/chakt-windows-x64.jar