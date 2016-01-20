package gui;

import algorithm.CommonService;
import algorithm.Histogram;
import algorithm.ThresholdAlg;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafxext.RubberBandSelection;
import org.opencv.core.*;
import org.opencv.video.Video;
import video.CameraCapture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static algorithm.CommonService.getImageArray;

/**
 * Created by Samsung on 1/20/2016.
 */
public class MeanShiftGUI extends AbstractGUI {

    private ImageView backProjectionImageView;

    private CameraCapture cameraCapture = new CameraCapture(PATH_TO_VIDEOS + "/video.mp4");
    private RubberBandSelection rubberBandSelection;

    private Slider binsSlider;
    private Button startVideo;

    private Mat firstFrame;
    private Mat roiHisto;

    private ScheduledExecutorService timer;

    Runnable task;

    @Override
    public void prepareStart() throws Exception {
        cameraCapture.testRead();
        firstFrame = cameraCapture.grabFrame();
        originalImage = new ImageView(createImage(firstFrame));
        Group imageLayer = new Group();
        imageLayer.getChildren().add(originalImage);
        rubberBandSelection = new RubberBandSelection(imageLayer);
        binsSlider = createSlider(3, 100);
        backProjectionImageView = new ImageView();
        startVideo = new Button("start");
        startVideo.setOnAction(event -> this.timer.scheduleAtFixedRate(task, 0, 60, TimeUnit.MILLISECONDS));
        addRow(createHbox(binsSlider, startVideo));
        addRow(createHbox(imageLayer, backProjectionImageView));

         task = () -> {
            Mat grabbedFrame = cameraCapture.grabFrame();
            Bounds bounds = rubberBandSelection.getBounds();
            Rect rect = new Rect((int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getWidth(),(int) bounds.getHeight());
            Mat backProjImage = Histogram.backProjection(grabbedFrame, roiHisto);
            Video.meanShift(backProjImage, rect, new TermCriteria(1, 30, 2));

            Core.rectangle(grabbedFrame, rect.tl(), rect.br(), new Scalar(122,122,122));
            Platform.runLater(() -> backProjectionImageView.setImage(createImage(grabbedFrame)));
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();

    }

    @Override
    public void refreshAllImages() {
        originalImage.setImage(createImage(firstFrame));
        int xLower = (int) rubberBandSelection.getBounds().getMinX();
        int xUpper = (int) rubberBandSelection.getBounds().getMaxX();
        int yLower = (int) rubberBandSelection.getBounds().getMinY();
        int yUpper = (int) rubberBandSelection.getBounds().getMaxY();
        Mat imageCut = CommonService.getROI(firstFrame, xLower, yLower, xUpper, yUpper);
        imageCut = CommonService.
        roiHisto = Histogram.getImagesHistogram(imageCut, (int) binsSlider.getValue());
        backProjectionImageView.setImage(createImage(Histogram.backProjection(firstFrame, roiHisto)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
