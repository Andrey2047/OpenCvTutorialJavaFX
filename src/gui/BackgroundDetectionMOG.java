package gui;

import algorithm.CodebookBcgDetectionStrategy;
import algorithm.ThresholdAlg;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;
import org.opencv.video.BackgroundSubtractorMOG2;
import video.CameraCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Samsung on 12/27/2015.
 */
public class BackgroundDetectionMOG extends AbstractGUI{
    public static final String PATH_TO_VIDEOS = "src/resources/video/";
    private AtomicInteger frameCounter = new AtomicInteger();

    final ImageView cameraView = new ImageView();
    final ImageView backgroundImage = new ImageView();

    Button startToShow = new Button("start");

    private CameraCapture cameraCapture = new CameraCapture();

    private ScheduledExecutorService timer;

    private BackgroundSubtractorMOG2 backgroundSubtractor = new BackgroundSubtractorMOG2();

    private Slider learningRateSlider = new Slider();
    private volatile double learningRate = 0;


    @Override
    public void prepareStart() throws Exception {
        cameraCapture.testRead();
        Mat dst = cameraCapture.grabFrame();
        cameraView.setImage(createImage(dst));

        learningRateSlider = createSlider(0, 1);
        addRow(createHbox(createScrollPane(cameraView), startToShow));
        addRow(createHbox(createScrollPane(backgroundImage), learningRateSlider));

        Mat fgmask = new Mat(dst.rows(), dst.cols(), CvType.CV_8UC1);

        for(int i=0; i< 50; i++){
            Mat grabbedFrame = cameraCapture.grabFrame();
            long startTime = System.currentTimeMillis();
            backgroundSubtractor.apply(grabbedFrame, fgmask, 0.1);
            Thread.currentThread().sleep(30);
        }
        backgroundImage.setImage(createImage(fgmask));

        Runnable task = () -> {
            Mat grabbedFrame = cameraCapture.grabFrame();
            backgroundSubtractor.apply(grabbedFrame, fgmask, learningRateSlider.getValue());
            final Mat diff3 = ThresholdAlg.castToThreeChannel(fgmask);
            Mat res = new Mat();
            Core.bitwise_and(grabbedFrame, diff3, res);
            Platform.runLater(() -> backgroundImage.setImage(createImage(res)));
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(task, 0, 33, TimeUnit.MILLISECONDS);

    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
