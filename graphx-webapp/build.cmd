@echo off
echo Installing graphx-webapp
call mvn clean install -f %~dp0\pom.xml
start %userprofile%\.m2\repository\com\yinker\tinyv\graphx-webapp