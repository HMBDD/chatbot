SET mypath=%~dp0
cd %mypath%
SET driverpath=%~d0
%driverpath%
SET mynewpath=%cd%
SET reportMovementBatFilePath=%mypath%lib\ReportMovement.bat
SET configFilepath=%mynewpath%\executionConfig.csv
For /F "tokens=1,2,3,4 delims=," %%i in (%configFilepath%) do call :execute %%i %%j %%k %%l

:execute
	Set "execute=%~1"
	Set "executeOn=%~2"
	Set "browser=%~3"
	Set "suiteFile=%~4"	
	IF "%execute%"=="" GOTO :EOF
	if %execute% == Y (
	if %executeOn% NEQ executeOn (
	echo test -Dbrowser=%browser% -DsuiteFile=%suiteFile%
	mvn test -Dbrowser=%browser% -DsuiteFile=%suiteFile%
	timeout 3
	)
	)
	
:end
goto :eof