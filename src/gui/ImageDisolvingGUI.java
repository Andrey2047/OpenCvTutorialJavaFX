package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import static algorithm.FindContours.plotContours;

/**
 * Created by andriiko on 12/2/2015.
 */
public class ImageDisolvingGUI extends AbstractGUI {

    public static final String IMAGE_1 = PATH_TO_IMAGES + "dog2" + ".jpg";
    public static final String IMAGE_2 = PATH_TO_IMAGES + "hand" + ".jpg";

    static Slider alfaKoeffSlider;
    static Slider gammaKoeffSlider;

    static ImageView resultImageView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        HBox row1 = new HBox();
        HBox row2 = new HBox();

        alfaKoeffSlider = createBaseSlider(0, 1);
        gammaKoeffSlider = createBaseSlider(0, 1);

        row1.getChildren().add(new ImageView(createImage(getImageArray(IMAGE_1))));
        row1.getChildren().add(new ImageView(createImage(getImageArray(IMAGE_2))));

        Mat dst = new Mat();
        Mat submat1 = getImageArray(IMAGE_1).submat(new Range(0, 160), new Range(0, 250));
        Mat submat2 = getImageArray(IMAGE_2).submat(new Range(0, 160), new Range(0, 250));
        Core.addWeighted(submat1, 1, submat2, 0, 0.0, dst);
        resultImageView =  new ImageView(createImage(dst));

        VBox slidersBox = new VBox();

        slidersBox.getChildren().addAll(new Label("alfa"), alfaKoeffSlider, new Label("gamma"), gammaKoeffSlider);

        row2.getChildren().addAll(slidersBox, resultImageView);

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
                double alfa = sli.getValue();
                Mat dst = new Mat();

                Mat imageArray1 = getImageArray(IMAGE_1);;
                Mat imageArray2 = getImageArray(IMAGE_2);

                double gamma = gammaKoeffSlider.getValue();
                Core.addWeighted(imageArray1.submat(new Range(0, 160), new Range(0, 250)), alfa, imageArray2.submat(new Range(0, 160), new Range(0, 250)), 1 - alfa, gamma, dst);
                resultImageView.setImage(createImage(dst));
            }
        });
        return sli;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
