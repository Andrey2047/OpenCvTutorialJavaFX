package gui;

import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Mat;

import static algorithm.CommonService.getImageArray;
import static algorithm.ThresholdAlg.threshold;

/**
 * Created by Samsung on 12/8/2015.
 */
public class ThresholdGUI extends AbstractGUI {

    public static final String CURRENT_IMAGE = PATH_TO_IMAGES + "car" + ".jpg";

    static ChoiceBox thresholdTYpe;
    static TextField maxValueField;
    static Slider thresholdValue;

    static ImageView originalImageView;
    static ImageView thresholdImageView;


    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        stage.setHeight(700);
        stage.setWidth(1200);

        HBox row1 = new HBox();

        thresholdTYpe = new ChoiceBox(FXCollections.observableArrayList("0", "1", "2", "3", "4"));
        thresholdTYpe.setValue("0");

        thresholdTYpe.addEventHandler(EventType.ROOT, event -> refreshAllImages());
        maxValueField = new TextField("255");
        maxValueField.addEventHandler(EventType.ROOT, e -> refreshAllImages());
        thresholdValue = createSlider(0,255);

        originalImageView = new ImageView(createImage(getImageArray(CURRENT_IMAGE)));
        thresholdImageView = new ImageView(createImage(threshold(getImageArray(CURRENT_IMAGE), 40, 255, 0)));

        VBox imageVbox = new VBox();
        imageVbox.getChildren().addAll(thresholdImageView, createHbox(new Label("maxValue"), maxValueField),
                createHbox(new Label("value"), thresholdValue),
                createHbox(new Label("type"), thresholdTYpe));

        row1.getChildren().addAll(originalImageView, imageVbox);
        root.getChildren().addAll(row1);
        stage.setTitle("Canny Alg");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();
    }


    private Slider createSlider(int min, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.addEventHandler(EventType.ROOT, event -> {
            refreshAllImages();
        });
        return slider;
    }

    public void refreshAllImages(){
        int maxValue = Integer.valueOf(maxValueField.getText());
        int thValue = (int)(thresholdValue.getValue());
        int type = Integer.valueOf(thresholdTYpe.getValue().toString());

        Mat imageArray = getImageArray(CURRENT_IMAGE);//CannyAlg.execute(CURRENT_IMAGE, 10, 100, 3, true);
        Mat filteredImage = threshold(imageArray, thValue, maxValue, type);
        thresholdImageView.setImage(createImage(filteredImage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
