package gui;

import algorithm.ThresholdAlg;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import video.BackgroundCaptor;
import video.CameraCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Samsung on 12/27/2015.
 */
public class BackgroundDetectionCodebook extends AbstractGUI{
    public static final String PATH_TO_VIDEOS = "src/resources/video/";

    final ImageView cameraView = new ImageView();

    Button startToShow = new Button("start");

    private CameraCapture cameraCapture = new CameraCapture(PATH_TO_VIDEOS+"video.mp4");

    private ScheduledExecutorService timer;


    @Override
    public void prepareStart() throws Exception {
        cameraCapture.testRead();
        Mat dst = cameraCapture.grabFrame();
        cameraView.setImage(createImage(dst));

        addRow(createHbox(createScrollPane(cameraView), startToShow));

        Runnable task = () -> {
            Mat dst1 = cameraCapture.grabFrame();
            Platform.runLater(() -> cameraView.setImage(createImage(dst1)));
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(task, 0, 66, TimeUnit.MILLISECONDS);

    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
