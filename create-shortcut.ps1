$WScriptShell = New-Object -ComObject WScript.Shell
$Shortcut = $WScriptShell.CreateShortcut([System.Environment]::GetFolderPath('Desktop') + '\PowerFit Gym.lnk')
$Shortcut.TargetPath = 'C:\Windows\System32\cmd.exe'
$Shortcut.Arguments = '/k "set PATH=D:\IntelliJ IDEA 2025.2.2\jbr\bin;D:\IntelliJ IDEA 2025.2.2\plugins\maven\lib\maven3\bin;%PATH% && cd /d C:\Users\Jelly\Desktop\Gym\gym && mvn clean package -DskipTests -q && echo ================================ && echo   http://localhost:8765 && echo   admin / admin123 && echo ================================ && start msedge http://localhost:8765 && java -jar target\gym-management-1.0.0.jar"'
$Shortcut.WorkingDirectory = 'C:\Users\Jelly\Desktop\Gym\gym'
$Shortcut.Save()
Write-Output 'Done'
