@echo off
echo Cleaning up Java Faces...

if exist eigenfaces (
  echo Deleting eigenfaces directory
  rd /s /q eigenfaces
)

if exist reconstructed (
  echo Deleting reconstructed images directory
  rd /s /q reconstructed
)

if exist eigen.cache (
  echo Deleting eigen.cache
  del eigen.cache
)

rem echo Deleting Java class files
rem del *.class

echo Finished.
