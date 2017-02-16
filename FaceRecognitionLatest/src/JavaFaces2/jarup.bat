@echo off

echo Creating JavaFaces2.jar...
jar cvf JavaFaces2.jar *.class

rem echo Deleting Java class files
rem del *.class

echo Finished.
