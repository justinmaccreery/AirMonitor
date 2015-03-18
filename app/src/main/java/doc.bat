@echo off

set target=C:\android\projects\AirMonitor\doc

if not exist "%target%" md "%target%"

for %%f in ("%target%\*.*") do (del /q "%%f")

for /d %%d in ("%target%\*") do (rd /q /s "%%d")

rem pause

javadoc -private -verbose -d doc"%target%" -link "http://docs.oracle.com/javase/8/docs/api/" @packages.txt > "%target%\doc.log" 2>&1