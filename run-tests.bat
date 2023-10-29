rd .\allure-results /q /s
@REM call mvn -Dgroups=TestRun clean test
call mvn clean test
mkdir .\allure-results\history\
copy .\allure-report\history\ .\allure-results\history\
call allure generate --clean
call allure open