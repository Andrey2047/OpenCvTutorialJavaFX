package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Range;

import static algorithm.CommonService.getImageArray;

/**
 * Created by andriiko on 12/2/2015.
 */
public class ImageBrightnessTransformingGUI extends AbstractGUI {

    public static final String IMAGE_1 = PATH_TO_IMAGES + "dog" + ".jpg";

    static Slider alfaKoeffSlider;
    static Slider bKoeffSlider;

    static ImageView resultImageView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        HBox row1 = new HBox();
        HBox row2 = new HBox();

        alfaKoeffSlider = createBaseSlider(0, 10);
        bKoeffSlider = createBaseSlider(0, 100);

        Mat imageArray = getImageArray(IMAGE_1);
        Mat dst = new Mat();

        imageArray.convertTo(dst, -1, 1, 0);

        resultImageView =  new ImageView(createImage(dst));

        row1.getChildren().addAll(new ImageView(createImage(imageArray)), resultImageView);


        VBox slidersBox = new VBox();

        slidersBox.getChildren().addAll(new Label("contrast"), alfaKoeffSlider, new Label("brightness"), bKoeffSlider);

        row2.getChildren().add(slidersBox);

        root.getChildren().addAll(row1, row2);


        primaryStage.setTitle("Canny Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private Slider createBaseSlider(final int minVal, final int maxVal) {
        Slider sli = new Slider();
        sli.setMin(minVal);
        sli.setMax(maxVal);
        sli.setShowTickLabels(true);
        sli.setShowTickMarks(true);

        sli.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Mat dst = new Mat();
                double alfa = alfaKoeffSlider.getValue();
                double b = bKoeffSlider.getValue();
                Mat imageArray = getImageArray(IMAGE_1);
                imageArray.convertTo(dst, -1, alfa, b);
                resultImageView.setImage(createImage(dst));
            }
        });
        return sli;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
