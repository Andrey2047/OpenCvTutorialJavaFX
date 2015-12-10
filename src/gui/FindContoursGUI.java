package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static algorithm.CommonService.binarizeImage;
import static algorithm.CommonService.getImageArray;
import static algorithm.FindContours.*;

/**
 * Created by Samsung on 11/29/2015.
 */
public class FindContoursGUI extends AbstractGUI {

    static String currentImage = "shapes";

    static Slider lowBinaryLevel;
    static Slider highBinaryLevel;

    static ImageView binarizedImageView;
    static ImageView imageWithContours;
    static ImageView imageWithShapes;
    static ImageView imageViewOrigin;

    static Mat binarizedImageMat;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Core.inRange();
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        HBox row1 = new HBox();
        HBox row2 = new HBox();

        Mat imageArray = getImageArray(PATH_TO_IMAGES + currentImage + ".jpg");
        imageViewOrigin = new ImageView(createImage(imageArray));
        binarizedImageMat = binarizeImage(imageArray, 40, 140);
        binarizedImageView = new ImageView(createImage(binarizedImageMat));
        lowBinaryLevel = createBinarSlider();
        highBinaryLevel = createBinarSlider();

        VBox binarizedImageHBox = new VBox();
        binarizedImageHBox.getChildren().addAll(binarizedImageView, lowBinaryLevel, highBinaryLevel);

        row1.getChildren().addAll(imageViewOrigin, binarizedImageHBox);

        imageWithContours = new ImageView(createImage(plotContours(findContours(binarizedImageMat, 0, 0), imageArray, new Scalar(255, 255, 0))));
        imageWithShapes = new ImageView(createImage(plotContours(findCircles(binarizedImageMat, 0, 0), imageArray, new Scalar(255, 255, 0))));
        row2.getChildren().addAll(imageWithContours, imageWithShapes);
        root.getChildren().addAll(row1, row2);
        primaryStage.setTitle("Canny Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private Slider createBinarSlider() {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(255);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int lowLevel = (int) lowBinaryLevel.getValue();
                int highLevel = (int) highBinaryLevel.getValue();
                binarizedImageMat = binarizeImage(getImageArray(PATH_TO_IMAGES + currentImage + ".jpg"), lowLevel, highLevel);
                binarizedImageView.setImage(createImage(binarizedImageMat));
                imageWithContours.setImage(createImage(plotContours(findContours(binarizedImageMat, 0, 0), getImageArray(PATH_TO_IMAGES + currentImage + ".jpg"), new Scalar(255, 255, 0))));
                Map<String, List<MatOfPoint>> shapeContours = new HashMap<>();
                shapeContours.put("triangle", findTriangles(binarizedImageMat, 0, 0));
                shapeContours.put("cirlce", findCircles(binarizedImageMat, 0, 0));
                imageWithShapes.setImage(createImage(plotContours(shapeContours, getImageArray(PATH_TO_IMAGES + currentImage + ".jpg"))));
            }
        });
        return slider;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
