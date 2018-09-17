SET mypath=%~dp0
cd %mypath%
SET driverpath=%~d0
%driverpath%
cd..
SET mynewpath=%cd%
SET configFilepath=%mynewpath%\ExecutionConfig.properties
FOR /F "tokens=1,2 delims==" %%G IN (%configFilepath%) DO (set %%G=%%H)  
echo test -DexecuteOn=%executeOn% -DbsEnvironment=%bsEnvironment% -Ddevice=%device% -DsuiteFile=%suiteFile%
mvn test -DexecuteOn=%executeOn% -DbsEnvironment=%bsEnvironment% -Ddevice=%device% -DsuiteFile=%suiteFile%
goto :eof