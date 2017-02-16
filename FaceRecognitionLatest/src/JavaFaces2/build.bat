@echo off
echo Building eigenfaces...

java -cp "lib/colt.jar;." BuildEigenFaces %*

echo Finished.
