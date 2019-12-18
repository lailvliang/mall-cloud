@echo on
@echo =============================================================
@echo $                                                           $
@echo $               hzed Microservices-Platform                  $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  hzed All Right Reserved                                   $
@echo $  Copyright (C) 2019-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title hzed Microservices-Platform
@color 0e

set /p version=������汾��:

call mvn versions:set -DnewVersion=%version%

call mvn versions:commit

pause