SET mypath=%~dp0
cd %mypath%
SET driverpath=%~d0
%driverpath%
SET jarfilepath=%mypath%
SET reportMovementjarFile=ReportMovement.jar
cd..
SET mynewpatah=%cd%
echo %mynewpatah%
SET reportFolderpath=%mynewpatah%\Reports
SET reportTempFolderPath=%mynewpatah%\target\surefire-reports
echo %reportTempFolderPath%
cd %mypath%
java -jar %reportMovementjarFile% %reportTempFolderPath% %reportFolderpath% testoutputfiles
goto :eof