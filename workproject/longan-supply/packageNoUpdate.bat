@echo off
call mvn clean
call mvn  -Dmaven.test.skip=true package -Pproduct
@pause