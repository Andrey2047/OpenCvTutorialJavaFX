import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;

import java.nio.ByteBuffer;

/**
 * Created by Samsung on 12/20/2015.
 */
public class WebCamTracker implements Runnable {

    FrameGrabber grabber = new OpenCVFrameGrabber(1);

    public WebCamTracker(){
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    public ByteBuffer getImageFromCam() throws FrameGrabber.Exception {
        return (ByteBuffer) grabber.grab().image[0];
    }

    public void run() {

    }
}
