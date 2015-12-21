package gui;

import algorithm.CannyAlg;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import java.util.concurrent.*;

/**
 * Created by andriiko on 12/21/2015.
 */
public class VideoTest extends AbstractGUI {

    final ImageView cameraView = new ImageView();
    VideoCapture grabber = new VideoCapture(0);
    Button startCamera;
    Button stopCamera;
    private ScheduledExecutorService timer;

    @Override
    public void prepareStart() throws Exception {
        grabber.grab();
        Mat dst = new Mat();
        grabber.read(dst);
        cameraView.setImage(createImage(dst));
        startCamera = new Button("start");
        startCamera.addEventHandler(EventType.ROOT, e -> handlePress());
        this.timer = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            Mat dst1 = new Mat();
            grabber.grab();
            grabber.retrieve(dst1);
            Platform.runLater(() -> cameraView.setImage(createImage(dst1)));
        };

        this.timer.scheduleAtFixedRate(task, 0, 33, TimeUnit.MILLISECONDS);

        stopCamera = new Button("stop");
        stopCamera.addEventHandler(EventType.ROOT, e -> handlePressStop());
        addRow(createHbox(cameraView, startCamera, stopCamera));
    }

    private void handlePressStop() {
        timer.te
    }

    private void handlePress() {


    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
