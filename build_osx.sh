./gradlew desktop:dist && \
java -jar ../packr-4.0.0.jar \
     --classpath desktop/build/libs/desktop-1.0.jar \
     --executable "Tower Defense Prototype" \
     --icon desktop/icon.icns \
     --jdk https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.1%2B12/OpenJDK21U-jre_x64_mac_hotspot_21.0.1_12.tar.gz \
     --mainclass software.greysky.towerdefense.DesktopLauncher \
     --output osx-dist \
     --platform mac \
     --resources desktop/build/resources \
     --useZgcIfSupportedOs \
     --vmargs Xmx1G XstartOnFirstThread && \
mv osx-dist TowerDefensePrototype.app
