package gui;

import algorithm.ThresholdAlg;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Arrays;

import static algorithm.CommonService.getImageArray;
import static algorithm.Histogram.backProjection;
import static algorithm.Histogram.getImagesHistogram;


/**
 * Created by Samsung on 12/13/2015.
 */
public class FindHandGUI extends AbstractGUI {

    private static final String HAND_IMAGE = PATH_TO_IMAGES + "hand.jpg";
    private static final String MAN_IMAGE = PATH_TO_IMAGES + "hands.jpg";

    Slider binsSlider;
    ImageView handImage;
    ImageView resultImage;
    Slider maxValueField;
    Slider thresholdValue;

    @Override
    public void prepareStart() throws Exception {
        binsSlider = createSlider(2, 150);
        maxValueField = createSlider(1, 255);
        thresholdValue = createSlider(1, 255);

        Mat manImageMat = getImageArray(MAN_IMAGE);
        originalImage = new ImageView(createImage(manImageMat));
        Mat handImageArray = getImageArray(HAND_IMAGE);
        handImage = new ImageView(createImage(handImageArray));
        resultImage = new ImageView(createImage(getResultImage(manImageMat, handImageArray)));


        addRow(createHbox(createScrollPane(resultImage),
                createVbox(handImage,
                        createHbox(new Label("beans"), binsSlider),
                        createHbox(new Label("maxValue"), maxValueField),
                        createHbox(new Label("thValue"), thresholdValue))));
       // addRow(createHbox(createVbox(resultImage, binsSlider)));
    }

    private Mat getResultImage(Mat manImageMat, Mat handImageArray) {
        Mat backProjection = getBackProjection(manImageMat, handImageArray);
        int maxValue = (int) maxValueField.getValue();
        int thValue = (int) thresholdValue.getValue();
        backProjection = ThresholdAlg.thresholdAndCastToThreeChanel(backProjection, thValue, maxValue, 0);
        Mat dst = new Mat();
        Core.bitwise_and(manImageMat, backProjection, dst);

        return dst;
    }

    private Mat getBackProjection(Mat manImageMat, Mat handImageArray) {
        int beansValue = (int)binsSlider.getValue();
        return backProjection(manImageMat, getImagesHistogram(Arrays.asList(handImageArray), beansValue));
    }

    @Override
    public void refreshAllImages() {
        resultImage.setImage(createImage(getResultImage(getImageArray(MAN_IMAGE), getImageArray(HAND_IMAGE))));
    }

    public static void main(String[] args) {
        launch(args);
    }


}
