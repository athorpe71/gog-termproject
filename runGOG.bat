@echo off
Echo =============================================
echo         Run Glory of Generals
Echo =============================================
:loop
del GloryOfGenerals.class
set /p user_name=Enter to run:
Echo GOG is now running...
Echo.
javac GloryOfGenerals.java
java GloryOfGenerals
Echo.
goto loop
pause
