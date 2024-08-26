@echo off
title Minecraft Development Console
echo Starting development KIT
rmdir C:\Users\NaysKutzu\Documents\GitHub\McPanelX-Core\target /s /q
:: Replace this with your own command from idea and add this at the end: -file pom.xml
C:\Users\NaysKutzu\.jdks\corretto-22.0.2\bin\java.exe -Dmaven.multiModuleProjectDirectory=C:\Users\NaysKutzu\IdeaProjects\McPanelX -Djansi.passthrough=true "-Dmaven.home=C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\plugins\maven\lib\maven3" "-Dclassworlds.conf=C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\plugins\maven\lib\maven3\bin\m2.conf" "-Dmaven.ext.class.path=C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\plugins\maven\lib\maven-event-listener.jar" "-javaagent:C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\lib\idea_rt.jar=8314:C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath "C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\plugins\maven\lib\maven3\boot\plexus-classworlds-2.8.0.jar;C:\Users\NaysKutzu\AppData\Local\Programs\IntelliJ IDEA Ultimate\plugins\maven\lib\maven3\boot\plexus-classworlds.license" org.codehaus.classworlds.Launcher -Didea.version=2024.2.0.2 package
echo Deleting the old plugin
rmdir development\plugins\McPanelX-Core /s /q
del development\plugins\McPanelX*.jar /f /s > nul
echo Copying the new plugin file...
xcopy target\McPanelX*.jar development\plugins\ /s /q
cd development
setlocal
C:\Users\NaysKutzu\.jdks\corretto-22.0.2\bin\java.exe -jar BungeeCord.jar
echo Server is dead!
exit