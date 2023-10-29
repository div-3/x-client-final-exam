rd .\allure-results /q /s
call mvn -Dgroups=TestRun clean test
@REM call mvn clean test
mkdir .\allure-results\history\
copy .\allure-report\history\ .\allure-results\history\
call allure generate --clean
call allure open