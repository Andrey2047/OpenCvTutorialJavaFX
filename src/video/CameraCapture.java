package video;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Samsung on 12/21/2015.
 */
public class CameraCapture {

    private VideoCapture capture = new VideoCapture(0);

    public void testRead(){
        capture.grab();
        capture.read(new Mat());
        capture.read(new Mat());
    }

    public synchronized Mat grabFrame() {
        Mat frame = new Mat();

        if (capture.isOpened()) {
            try {
                capture.grab();
                capture.read(frame);
                if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                }

            } catch (Exception e) {
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }
}
