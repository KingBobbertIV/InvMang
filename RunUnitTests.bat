@echo off

call SetClasspath

REM To get console-mode results from unit test execution, use the following instead:

REM   java junit.swingui.TestRunner comp3350.team8.test.AllTests

@echo on

java junit.textui.TestRunner comp3350.team8.test.AllTests > AllTests.txt
