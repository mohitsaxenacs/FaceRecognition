
// FaceTracker.java
// Andrew Davison, July 2013, ad@fivedots.psu.ac.th

/* Show a sequence of images snapped from a webcam in a picture panel (FacePanel). 
   A face is highlighted with a yellow rectangle, which is updated as the face
   moves. The highlighted part of the image can be saved by the user pressing
   the "Save Face" button.

   Usage:
      > java FaceTracker
*/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;


public class FaceTracker extends JFrame 
{
  // GUI components
  private FacePanel facePanel;


  public FaceTracker()
  {
    super("Face Tracker");

    Container c = getContentPane();
    c.setLayout( new BorderLayout() );   

    // Preload the opencv_objdetect module to work around a known bug.
    Loader.load(opencv_objdetect.class);

    facePanel = new FacePanel(); // the sequence of pictures appear here
    c.add( facePanel, BorderLayout.CENTER);

    // button for saving a highlighted face
    JButton saveBut = new JButton("Save Face");
    saveBut.addActionListener( new ActionListener() {
       public void actionPerformed(ActionEvent e)
       { facePanel.saveFace();  }
    });

    JPanel p = new JPanel();
    p.add(saveBut);
    c.add(p, BorderLayout.SOUTH);


    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      { facePanel.closeDown();    // stop snapping pics
        System.exit(0);
      }
    });

    setResizable(false);
    pack();  
    setLocationRelativeTo(null);
    setVisible(true);
  } // end of FaceTracker()


  // -------------------------------------------------------

  public static void main( String args[] )
  {  new FaceTracker();  }

} // end of FaceTracker class
