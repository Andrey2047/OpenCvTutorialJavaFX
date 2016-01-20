package gui;

import algorithm.CodebookBcgDetectionStrategy;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import video.CameraCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Samsung on 12/27/2015.
 */
public class BackgroundDetectionCodebook extends AbstractGUI{
    public static final String PATH_TO_VIDEOS = "src/resources/video/";
    private AtomicInteger frameCounter = new AtomicInteger();

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

        CodebookBcgDetectionStrategy alg = new CodebookBcgDetectionStrategy();

        Runnable task = () -> {
            Mat detectedFrame = cameraCapture.grabFrame();
            alg.updateCodebook(detectedFrame);
            if(frameCounter.incrementAndGet() % 8 == 0){
                alg.clearStaleEntries();
            }
            System.out.println("Frame " + frameCounter.get() + " handled");
            //Platform.runLater(() -> cameraView.setImage(createImage(detectedFrame)));
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(task, 0, 120, TimeUnit.MILLISECONDS);

    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        //launch(args);
        AtomicInteger frameCounter = new AtomicInteger();
        CameraCapture cameraCapture = new CameraCapture(PATH_TO_VIDEOS+"video.mp4");
        cameraCapture.testRead();
        Mat dst = cameraCapture.grabFrame();
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        CodebookBcgDetectionStrategy alg = new CodebookBcgDetectionStrategy();

        for(;;) {
            Mat detectedFrame = cameraCapture.grabFrame();
            alg.updateCodebook(detectedFrame);
            if (frameCounter.incrementAndGet() % 8 == 0) {
                alg.clearStaleEntries();
            }
            System.out.println("Frame " + frameCounter.get() + " handled");
        }
    }
}
