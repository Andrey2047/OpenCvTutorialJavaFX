package gui;

import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
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
        lowThresholdView.setImage(createImage(dst));
        highThresholdView.setImage(createImage(dst));

        addRow(createHbox(cameraView, startToShow));
        addRow(createHbox(lowThresholdView, highThresholdView));

        timer = Executors.newScheduledThreadPool(2);
        Runnable learningTask = () -> {
            Mat dst1 = cameraCapture.grabFrame();
            backgroundCaptor.accumulateBackground(dst1);
            if(backgroundCaptor.getAccumulator() % 10000 == 0){
                backgroundCaptor.normalizeAccumulators();
            }
            Platform.runLater(() -> cameraView.setImage(createImage(dst1)));
        };
        timer.scheduleAtFixedRate(learningTask, 0, 100, TimeUnit.MILLISECONDS);

        startToShow.addEventHandler(EventType.ROOT, event -> {
            Runnable displayTask = () -> {
                Mat dst1 = cameraCapture.grabFrame();
                Mat lowTh = backgroundCaptor.getLowForeground();
                Mat high = backgroundCaptor.getHighForeground();
                Platform.runLater(() -> {
                    cameraView.setImage(createImage(dst1));
                    lowThresholdView.setImage(createImage(lowTh));
                    highThresholdView.setImage(createImage(high));
                });
            };
            timer.scheduleAtFixedRate(displayTask, 0, 33, TimeUnit.MILLISECONDS);
        });
    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
