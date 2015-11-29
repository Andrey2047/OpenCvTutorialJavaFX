package gui;

import algorithm.CommonService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static algorithm.CommonService.getImageArray;

/**
 * Created by Samsung on 11/29/2015.
 */
public class FindContoursGUI extends AbstractGUI {

    static String currentImage = "dog";

    static Slider lowBinaryLevel;
    static Slider highBinaryLevel;

    static ImageView binarizedImageView;
    static ImageView imageViewOrigin;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Core.inRange();
        HBox root = new HBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        Mat imageArray = getImageArray(PATH_TO_IMAGES + currentImage + ".jpg");
        imageViewOrigin = new ImageView(createImage(imageArray));
        binarizedImageView = new ImageView(createImage(CommonService.binarizeImage(imageArray, 40, 140)));
        lowBinaryLevel = createBinarSlider();
        highBinaryLevel = createBinarSlider();
        VBox binarizedImageHBox = new VBox();
        binarizedImageHBox.getChildren().addAll(binarizedImageView, lowBinaryLevel, highBinaryLevel);

        root.getChildren().addAll(imageViewOrigin, binarizedImageHBox);

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
                binarizedImageView.setImage(createImage(CommonService.binarizeImage(getImageArray(PATH_TO_IMAGES + currentImage + ".jpg"), lowLevel, highLevel)));
            }
        });
        return slider;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
