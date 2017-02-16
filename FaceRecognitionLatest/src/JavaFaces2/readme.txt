
NUI Chapter 8. Face Recognition : JavaFaces2

From the website:

  Killer Game Programming in Java
  http://fivedots.coe.psu.ac.th/~ad/jg

  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th


If you use this code, please mention my name, and include a link
to the website.

Thanks,
  Andrew

============================

This directory contains two Java applications:
  *  BuildEigenFaces and FaceRecognition

which are built from 9 Java files:
  *  BuildEigenFaces.java, FaceRecognition.java
     EigenvalueDecomp.java, Matrix2D.java, FaceBundle.java
     FileUtils.java, ImageUtils.java
     ImageDistanceInfo.java, MatchResult.java

There are 3 test images, used by FaceRecognizer:
  *  andrew0.png, jim0.png, watcharin0.png


There are 2 subdirectories:
  *  lib\                -- holds the COLT package as a JAR
                           (from http://acs.lbl.gov/software/colt/)

  *  trainingImages\     -- 25 example training images, used by BuildEigenFaces
         - a training image filename should be based on a name + number, and be a PNG file
         - e.g. andrew1.png


There are 5 batch files:
  *  compile.bat       // compile code

  *  build.bat         // runs BuildEigenFaces

  *  match.bat         // runs FaceRecognition

  *  cleanup.bat       // deletes eigenfaces\ reconstructed\ and eigen.cache 
       e.g. > cleanup

  *  jarup.bat         // packages all class files into JavaFaces2.jar
                       // JavaFaces2.jar is used by GUI Face Recognizer\ 
                          (copy over there if necessary)
       e.g. > jarup          


----------------------------
Compilation:

> compile *.java                     
     // uses lib\colt.jar 

----------------------------
Execution of BuildEigenFaces and FaceRecognition:

> build          // calls BuildEigenFaces
                          
  -  uses lib\colt.jar 
  -  reads training images from trainingImages\
  -  creates subdirectories eigenfaces\ and reconstructed\ 
        - eigenfaces\ will contain the generated eigenfaces
        - reconstructed\ will contain regenerated training images
              * check these to see if the code is working, then they can be deleted
  -  creates eigen.cache    
       - do NOT delete eigen.cache; it's needed by FaceRecognition
       - eigen.cache is used by GUI Face Recognizer\ (copy over there if necessary)



> match imagePngFnm [numberOfEigenfaces]          
                       // find a name for the face in imagePngFnm using FaceRecognition

  - uses lib\colt.jar and eigen.cache
  - does NOT use  eigenfaces\ or reconstructed\
  - imagePngFnm should be the same format as the training images 
    (e.g. same size, grayscale, same resolution, same face position, same brightness, etc)

e.g. 
 > match andrew0.png            // should return "andrew"
 > match jim0.png  20           // no match found; usually returns "peeranut" and a large distance
 > match watcharin0.png 24      // should return "watcharin"


----------------------------
Creating New Training Images

The easiest way of generating training images is to download the face tracker application
from http://fivedots.coe.psu.ac.th/~ad/jg/nui07/. It includes a "Save Face" button which will
save image clips of the face. These should be renamed to be based on a name + number, 
and be a PNG file (e.g. andrew1.png). Put them in trainingImages\

----------------------------
Last updated: 4th May 2011
