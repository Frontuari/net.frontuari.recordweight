@echo off

set DEBUG_MODE=

if "%1" == "debug" (
  set DEBUG_MODE=debug
)

cd net.frontuari.recordweight.targetplatform
call .\plugin-builder.bat %DEBUG_MODE% ..\net.frontuari.recordweight ..\net.frontuari.recordweight.test
cd ..
