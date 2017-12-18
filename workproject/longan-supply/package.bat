@echo off
rem call svn update
call mvn clean
call mvn  -Dmaven.test.skip=true package -Pdaily
@pause