@echo off
echo Installing CallDetailClient
call mvn clean install -f %~dp0\pom.xml
start %userprofile%\.m2\repository\com\yinker\xiaov\tinyv\CallDetailClient