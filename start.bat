@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ================================
echo   PowerFit Gym 一键启动
echo ================================

set "JAVA_HOME=D:\IntelliJ IDEA 2025.2.2\jbr"
set "MAVEN_HOME=D:\IntelliJ IDEA 2025.2.2\plugins\maven\lib\maven3"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo [1/2] 编译打包...
call "%MAVEN_HOME%\bin\mvn.cmd" clean package -DskipTests -q
if %errorlevel% neq 0 (
    echo 编译失败！
    pause
    exit /b 1
)

echo [2/2] 启动服务...
echo.
echo   访问地址: http://localhost:8765
echo   管理员: admin / admin123
echo.
start "" http://localhost:8765
"%JAVA_HOME%\bin\java.exe" -jar target\gym-management-1.0.0.jar
pause
