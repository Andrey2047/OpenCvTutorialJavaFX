package gui;

import algorithm.FindContours;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

import static algorithm.BlurFilters.simpleBlur;
import static algorithm.CommonService.getImageArray;
import static algorithm.ThresholdAlg.threshold;

/**
 * Created by andriiko on 12/10/2015.
 */
public class ConvexHullGUI extends AbstractGUI{

    ImageView blurIm;
    ImageView thresholdIm;

    Slider thresholdValueField;
    Slider thresholdMaxValueField;
    ChoiceBox thresholdTYpe;

    ImageView contoursIm;
    ImageView convexHullIm;

    public void prepareStart() throws Exception {
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        row2.setSpacing(20);

        HBox row3 = new HBox();
        row3.setSpacing(20);

        HBox row4 = new HBox();
        row4.setSpacing(20);

        populateImageList();
        thresholdValueField = createBaseSlider(0, 255);
        thresholdMaxValueField = createBaseSlider(0, 255);
        thresholdTYpe = new ChoiceBox(FXCollections.observableArrayList("0", "1", "2", "3", "4"));
        thresholdTYpe.setValue("0");

        thresholdTYpe.addEventHandler(EventType.ROOT, event -> refreshAllImages());
        populateImageViews();
        row1.getChildren().add(imageList);

        row2.getChildren().addAll(originalImage, blurIm);

        VBox thImageVbox = new VBox();
        thImageVbox.getChildren().addAll(thresholdIm, createHbox(new Label("maxValue"), thresholdMaxValueField),
                createHbox(new Label("value"), thresholdValueField),
                createHbox(new Label("type"), thresholdTYpe));

        row3.getChildren().addAll(thImageVbox, contoursIm);
        row3.getChildren().addAll(convexHullIm);
        addRow(row1, row2, row3, row4);
    }

    private void populateImageViews() {
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage = new ImageView(createImage(imageArray));
        Mat blured = simpleBlur(imageArray);
        blurIm = new ImageView(createImage(blured));
        Mat threshold = threshold(blured, 20, 255, 0);
        thresholdIm = new ImageView(createImage(threshold));
        List<MatOfPoint> contourList = FindContours.findContours(threshold, 0,0);
        Mat imageWithContours = FindContours.plotContours(contourList, threshold, new Scalar(255, 255, 0));
        contoursIm = new ImageView(createImage(imageWithContours));

        List<MatOfPoint> convexHulls = FindContours.findConvexHull(contourList);
        List<MatOfPoint> combinedImage = new ArrayList<>();
        combinedImage.addAll(contourList);
        combinedImage.addAll(convexHulls);

        Mat imageWithContoursAndHulls = FindContours.plotContours(combinedImage, threshold, new Scalar(255, 255, 0));
        convexHullIm = new ImageView(createImage(imageWithContoursAndHulls));
    }



    private Slider createBaseSlider(final int minVal, final int maxVal) {
        Slider sli = new Slider();
        sli.setMin(minVal);
        sli.setMax(maxVal);
        sli.setShowTickLabels(true);
        sli.setShowTickMarks(true);
        sli.addEventHandler(EventType.ROOT, ev -> refreshAllImages());
        return sli;
    }

    public void refreshAllImages(){
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage.setImage(createImage(imageArray));
        Mat bluredImage = simpleBlur(imageArray);
        blurIm.setImage(createImage(bluredImage));

        int thValue = Integer.valueOf((int) thresholdValueField.getValue());
        int thMaxValue = Integer.valueOf((int) thresholdMaxValueField.getValue());
        int type = Integer.valueOf(thresholdTYpe.getValue().toString());

        Mat thresholdImage = threshold(bluredImage, thValue, thMaxValue, type);
        thresholdIm.setImage(createImage(thresholdImage));

        List<MatOfPoint> contourList = FindContours.findContours(thresholdImage, 0,0);
        Mat imageWithContours = FindContours.plotContours(contourList, thresholdImage, new Scalar(255, 255, 0));
        contoursIm.setImage(createImage(imageWithContours));

        List<MatOfPoint> convexHulls = FindContours.findConvexHull(contourList);
        List<MatOfPoint> combinedImage = new ArrayList<>();
        //combinedImage.addAll(contourList);
        combinedImage.addAll(convexHulls);

        Mat imageWithContoursAndHulls = FindContours.plotContours(combinedImage, thresholdImage, new Scalar(255, 0, 0));
        convexHullIm.setImage(createImage(imageWithContoursAndHulls));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
