import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bytedeco.javacv.FrameGrabber;

import java.io.ByteArrayInputStream;

/**
 * Created by Samsung on 12/20/2015.
 */
public class CameraGrabber extends AbstractGUI{

    WebCamTracker webCamTracker;

    final ImageView cameraView = new ImageView();

    @Override
    public void prepareStart() throws Exception {
        addRow(createHbox(cameraView));

        webCamTracker = new WebCamTracker();
        Thread t2 = new Thread(new CameraImageUpdater());
        t2.start();
    }

    @Override
    public void refreshAllImages() {

    }

    private class CameraImageUpdater implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    ByteArrayInputStream byteArrayInputStream =
                            new ByteArrayInputStream(webCamTracker.getImageFromCam().array());
                    Image image = new Image(byteArrayInputStream);
                    cameraView.setImage(image);
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
