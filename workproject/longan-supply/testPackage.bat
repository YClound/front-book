@echo off
call svn update
call mvn clean
call mvn  -Dmaven.test.skip=true package -Ptest
@pause