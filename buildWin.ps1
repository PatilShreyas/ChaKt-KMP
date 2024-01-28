New-Item -Path "distributions" -ItemType Directory
.\gradlew :composeApp:packageMsi
Copy-Item -Path composeApp/build/compose/binaries/main/msi/ChaKt-1.0.0.msi distributions/chakt.msi