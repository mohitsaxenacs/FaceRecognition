@echo off
echo Matching eigenfaces...

java -cp "lib/colt.jar;." FaceRecognition %*

echo Finished.
