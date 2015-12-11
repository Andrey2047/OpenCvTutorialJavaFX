package gui;

import algorithm.BlurFilters;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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

    static String currentImage = "shapes";

    static String currentImagePath = PATH_TO_IMAGES + currentImage + ".jpg";

    ImageView gaussianBlurView;

    TextField xKSizeGaussField;
    TextField yKSizeGaussField;

    Slider xSigmaGausField;
    Slider ySigmaGausField;

    ImageView medianBlurView;
    TextField medianKSizeField;

    ImageView bilateralBlurView;
    
    Slider sigmaColorSlider;
    Slider sigmaSpaceSlider;


    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        HBox row1 = new HBox();
        HBox row2 = new HBox();
        originalImage = new ImageView(createImage(getImageArray(currentImagePath)));
        row1.getChildren().addAll(originalImage, buildGausBox());
        row2.getChildren().addAll(buildMedianBlock(), buildBilateralFilterBlock());

        root.getChildren().addAll(row1, row2);
        primaryStage.setTitle("Canny Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private VBox buildBilateralFilterBlock(){
        VBox gBox = new VBox();
        Mat imageArray = getImageArray(currentImagePath);
        bilateralBlurView =  new ImageView(createImage(imageArray));
        sigmaColorSlider = createBilateralSlider(0, 1000);
        sigmaSpaceSlider = createBilateralSlider(0, 30);
        gBox.getChildren().addAll(bilateralBlurView, new HBox(new Label("color sigma"), sigmaColorSlider), new HBox(new Label("space sigma"), sigmaSpaceSlider));
        return gBox;
    }

    private VBox buildMedianBlock(){
        VBox gBox = new VBox();
        Mat imageArray = getImageArray(currentImagePath);
        medianBlurView = new ImageView(createImage(imageArray));
        medianKSizeField = new TextField("3");
        medianKSizeField.addEventHandler(KeyEvent.KEY_RELEASED, event->{
            int kSize = Integer.valueOf(medianKSizeField.getText());
            Mat blurImage = BlurFilters.medianBlur(currentImagePath, kSize);
            medianBlurView.setImage(createImage(blurImage));
        });
        gBox.getChildren().addAll(medianBlurView, new HBox(new Label("kSize"), medianKSizeField));
        return gBox;
    }

    private VBox buildGausBox() {
        VBox gBox = new VBox();
        Mat imageArray = getImageArray(currentImagePath);
        gaussianBlurView = new ImageView(createImage(imageArray));

        xKSizeGaussField = new TextField("3");
        xKSizeGaussField.addEventHandler(KeyEvent.KEY_RELEASED, event -> refreshGausImage());
        yKSizeGaussField = new TextField("3");
        yKSizeGaussField.addEventHandler(KeyEvent.KEY_RELEASED, event -> refreshGausImage());
        xSigmaGausField = createGausSlider(0, 100);
        ySigmaGausField = createGausSlider(0, 100);
        gBox.getChildren().addAll(gaussianBlurView,
                new HBox(new Label("xKSize"),xKSizeGaussField),
                new HBox(new Label("yKSize"), yKSizeGaussField),
                new HBox(new Label("xSigma"), xSigmaGausField),
                new HBox(new Label("ySigma"), ySigmaGausField));
        return gBox;
    }

    private Slider createBilateralSlider(int min, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((observableValue, number, t1) ->  refreshBilateralImage());
        return slider;
    }

    private Slider createGausSlider(int min, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((observableValue, number, t1) ->  refreshGausImage());
        return slider;
    }

    private void refreshGausImage() {
        double XSigma = xSigmaGausField.getValue();
        double YSigma = ySigmaGausField.getValue();
        int xKSize = Integer.valueOf(xKSizeGaussField.getText());
        int yKSize = Integer.valueOf(yKSizeGaussField.getText());
        Mat blurImage = BlurFilters.gausBlur(currentImagePath, xKSize, yKSize, XSigma, YSigma);
        gaussianBlurView.setImage(createImage(blurImage));
    }

    private void refreshBilateralImage() {
        double sigmaColor = sigmaColorSlider.getValue();
        double sigmaSpace = sigmaSpaceSlider.getValue();
        Mat blurImage = BlurFilters.bilateralBlur(currentImagePath, sigmaColor, sigmaSpace);
        bilateralBlurView.setImage(createImage(blurImage));
    }

    @Override
    public void refreshAllImages() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
