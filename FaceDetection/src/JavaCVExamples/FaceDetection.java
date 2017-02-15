package JavaCVExamples;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;


public class FaceDetection
{
  private static final int SCALE = 2;     
     // scaling factor to reduce size of input image

  // cascade definition for face detection
  private static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";

  private static final String OUT_FILE = "markedFaces.jpg";


  public static void main(String[] args)
  {
    if (args.length != 1) {
      System.out.println("Usage: run FaceDetection <input-file>");
      return;
    }

    // preload the opencv_objdetect module to work around a known bug
    Loader.load(opencv_objdetect.class); 

    // load an image
    System.out.println("Loading image from " + args[0]);
    IplImage origImg = cvLoadImage(args[0]);

    // convert to grayscale
    IplImage grayImg = cvCreateImage(cvGetSize(origImg), IPL_DEPTH_8U, 1);
    cvCvtColor(origImg, grayImg, CV_BGR2GRAY);  

    // scale the grayscale (to speed up face detection)
    IplImage smallImg = IplImage.create(grayImg.width()/SCALE, 
                                        grayImg.height()/SCALE, IPL_DEPTH_8U, 1);
    cvResize(grayImg, smallImg, CV_INTER_LINEAR);

    // equalize the small grayscale
	cvEqualizeHist(smallImg, smallImg);

    // create temp storage, used during object detection
    CvMemStorage storage = CvMemStorage.create();

    // instantiate a classifier cascade for face detection
    CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
                                                     cvLoad(CASCADE_FILE));
    System.out.println("Detecting faces...");
    CvSeq faces = cvHaarDetectObjects(smallImg, cascade, storage, 1.1, 3, 
                                        CV_HAAR_DO_CANNY_PRUNING);
                                        // CV_HAAR_DO_ROUGH_SEARCH);
                                        // 0);
    cvClearMemStorage(storage);

    // iterate over the faces and draw yellow rectangles around them
    int total = faces.total();
    System.out.println("Found " + total + " face(s)");
    for (int i = 0; i < total; i++) {
      CvRect r = new CvRect(cvGetSeqElem(faces, i));
      cvRectangle(origImg, cvPoint( r.x()*SCALE, r.y()*SCALE ),    // undo the scaling
                    cvPoint( (r.x() + r.width())*SCALE, (r.y() + r.height())*SCALE ), 
                        CvScalar.YELLOW, 6, CV_AA, 0);
    }

    if (total > 0) {
      System.out.println("Saving marked-faces version of " + args[0] + " in " + OUT_FILE);
      cvSaveImage(OUT_FILE, origImg);
    }
  }  // end of main()

}  // end of FaceDetection class
