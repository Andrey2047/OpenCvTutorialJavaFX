package gui;

import algorithm.BlurFilters;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Mat;


import static algorithm.CommonService.getImageArray;
import static algorithm.FindContours.plotContours;

/**
 * Created by andriiko on 12/3/2015.
 */
public class DifferentBlursGUI extends AbstractGUI{

    static String currentImage = "dog";

    static String currentImagePath = PATH_TO_IMAGES + currentImage + ".jpg";

    ImageView originalImage;
    ImageView gaussianBlurView;

    TextField xKSizeGaussField;
    TextField yKSizeGaussField;

    Slider xSigmaGausField;
    Slider ySigmaGausField;


    ImageView medianBlurView;
    ImageView bilateralBlurView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        HBox row1 = new HBox();
        HBox row2 = new HBox();

        buildGausBox();
    }

    private void buildGausBox() {
        VBox gBox = new VBox();
        Mat imageArray = getImageArray();
        gaussianBlurView = new ImageView(createImage(imageArray));
        xKSizeGaussField = new TextField("3");
        yKSizeGaussField = new TextField("3");

        xSigmaGausField = new Slider();


    }

    private Slider createGausSlider(int min, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double XSigma = xSigmaGausField.getValue();
                double YSigma = xSigmaGausField.getValue();
                int xKSize = Integer.valueOf(xKSizeGaussField.getText());
                int yKSize = Integer.valueOf(yKSizeGaussField.getText());
                Mat blurImage = BlurFilters.gausBlur(currentImagePath, xKSize, yKSize, XSigma, YSigma);
                gaussianBlurView.setImage(createImage(blurImage));
            }
        });
        return slider;
    }
}
