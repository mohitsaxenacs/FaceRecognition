@echo off
echo Compiling %* with Colt...

javac -cp "lib/colt.jar;." %*

echo Finished.
