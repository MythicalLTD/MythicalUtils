@echo off
title Minecraft Development Console
echo Starting development KIT
rmdir C:\Users\NaysKutzu\Documents\GitHub\McPanelX-Core\target /s /q
:: Replace this with your own command from idea and add this at the end: -file pom.xml
C:\Users\NaysKutzu\.jdks\corretto-22.0.1\bin\java.exe -Dmaven.multiModuleProjectDirectory=C:\Users\NaysKutzu\Documents\GitHub\McPanelX-Bungee -Djansi.passthrough=true "-Dmaven.home=C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\plugins\maven\lib\maven3" "-Dclassworlds.conf=C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\plugins\maven\lib\maven3\bin\m2.conf" "-Dmaven.ext.class.path=C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\plugins\maven\lib\maven-event-listener.jar" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\lib\idea_rt.jar=8796:C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\plugins\maven\lib\maven3\boot\plexus-classworlds-2.7.0.jar;C:\Program Files\JetBrains\IntelliJ IDEA 2024.1\plugins\maven\lib\maven3\boot\plexus-classworlds.license" org.codehaus.classworlds.Launcher -Didea.version=2024.1 package -file pom.xml
pause
echo Deleting the old plugin
rmdir development\plugins\McPanelX-Core /s /q
del development\plugins\McPanelX-Core-*.jar /f /s > nul
echo Copying the new plugin file...
xcopy target\McPanelX-Core-*.jar development\plugins\ /s /q
cd development
setlocal
java -jar BungeeCord.jar
echo Server is dead!
pause