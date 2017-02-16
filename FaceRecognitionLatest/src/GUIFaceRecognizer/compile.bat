@echo off
echo Compiling %* with JavaCV, OpenCV, JavaFaces2...

javac -cp "d:\javacv-bin\javacv.jar;d:\javacv-bin\javacpp.jar;d:\javacv-bin\javacv-windows-x86.jar;JavaFaces2.jar;." %*

echo Finished.
