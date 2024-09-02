import os
import platform

if platform.system() == "Windows":
    print("Running on Windows")    
    print("Starting development KIT")
    print("Deleting the old plugin target directory...")
    os.system("rmdir /s /q target > nul")
    print("Setting JAVA_HOME...")
    os.environ["JAVA_HOME"] = "development\\tools\\jdk-22.0.1"
    os.system("%JAVA_HOME%\\bin\\java.exe -version")
    print("Setting MAVEN_HOME...")
    os.environ["maven_home"] = "development\\tools\\apache-maven-3.9.9\\bin\\"
    current_path = os.getcwd()
    print("Current path:", current_path)

    print("Building the plugin...")
    os.system(f"{os.environ['maven_home']}mvn package -Dmaven.multiModuleProjectDirectory={current_path} -Djansi.passthrough=true -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Ddetail=false -Dsun.stderr.encoding=UTF-8 -file pom.xml")
    print("Plugin built!")

    print("Deleting the old plugin")
    os.system("rmdir development\\plugins\\McPanelX /s /q")
    os.system("del development\\plugins\\McPanelX*.jar /f /s > nul")

    print("Copying the new plugin file...")
    os.system("xcopy target\\McPanelX*.jar development\\plugins\\ /s /q")

    print("Going to the development directory...")
    serverpath= f"{current_path}\\development"
    print(serverpath)
    if (os.path.exists(serverpath)):
        print("Server directory exists")
    else:
        print("Server directory does not exist")
        exit()
    print("Starting the server...")
    if os.path.exists(os.path.join(serverpath, "FlameCord.jar")):
        os.system(f"cd {serverpath} && tools\\jdk-22.0.1\\bin\\java.exe -jar FlameCord.jar")
    else:
        os.system(f"cd {serverpath} && tools\\jdk-22.0.1\\bin\\java.exe -jar BungeeCord.jar")
else:
    # Linux specific code
    print("Running on Linux")
    print("This script is not supported on Linux")