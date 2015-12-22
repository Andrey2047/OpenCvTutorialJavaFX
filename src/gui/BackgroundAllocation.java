package gui;

import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import video.BackgroundCaptor;
import video.CameraCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Samsung on 12/21/2015.
 */
public class BackgroundAllocation extends AbstractGUI {

    final ImageView cameraView = new ImageView();
    final ImageView lowThresholdView = new ImageView();
    final ImageView highThresholdView = new ImageView();

    Button startToShow = new Button("start");

    private BackgroundCaptor backgroundCaptor = new BackgroundCaptor();
    private CameraCapture cameraCapture = new CameraCapture();

    private ScheduledExecutorService timer;


    @Override
    public void prepareStart() throws Exception {
        cameraCapture.testRead();
        Mat dst = cameraCapture.grabFrame();
        cameraView.setImage(createImage(dst));
        lowThresholdView.setImage(createImage(new Mat(640, 480, CvType.CV_32FC3, new Scalar(0,0,0))));
        highThresholdView.setImage(createImage(new Mat(640, 480, CvType.CV_32FC3, new Scalar(0,0,0))));

        addRow(createHbox(startToShow));
        addRow(createHbox(lowThresholdView, highThresholdView));

//        timer = Executors.newScheduledThreadPool(2);
//        Runnable learningTask = () -> {
//            Mat dst1 = cameraCapture.grabFrame();
//            backgroundCaptor.accumulateBackground(dst1);
//            Platform.runLater(() -> cameraView.setImage(createImage(dst1)));
//        };
//        timer.scheduleAtFixedRate(learningTask, 0, 100, TimeUnit.MILLISECONDS);

        for(int i=0; i < 1500; i++){
            Mat dst1 = cameraCapture.grabFrame();
            backgroundCaptor.accumulateBackground(dst1);
            Thread.sleep(10);
        }
        backgroundCaptor.normalizeAccumulators();
        //lowThresholdView.setImage(createImage(backgroundCaptor.getAvarageForeground()));
        highThresholdView.setImage(createImage(backgroundCaptor.getLowForeground()));

    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
