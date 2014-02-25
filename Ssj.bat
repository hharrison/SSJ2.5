@echo off

call %SSJHOME%\bin\Ssj.bat

set CLASSPATH=.;%SSJHOME%\build;%SSJHOME%\source;%CLASSPATH%
set SSJSRC=%SSJHOME%\source\umontreal\iro\lecuyer
set TEXINPUTS=.:%SSJHOME%\source:%TEXINPUTS%

perl %SSJHOME%\setl2hinit.pl %SSJHOME%\source
