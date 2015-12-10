package gui;

import algorithm.CannyAlg;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.Node;
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
import static algorithm.MorphologicalTransformationFunction.dialate;
import static algorithm.MorphologicalTransformationFunction.erode;
import static algorithm.MorphologicalTransformationFunction.transform;

/**
 * Created by Samsung on 12/8/2015.
 */
public class MorphTransformGUI extends AbstractGUI{

    public static final String CURRENT_IMAGE = PATH_TO_IMAGES + "text/text1" + ".bmp";

    static ChoiceBox erodeShapeBox;
    static TextField kSizeField;
    static Slider iterationNumberField;
    static ChoiceBox transformTypeBox;

    static ImageView originalImageView;
    static ImageView erodedImageView;


    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        stage.setHeight(700);
        stage.setWidth(1200);

        HBox row1 = new HBox();
        HBox row2 = new HBox();

        erodeShapeBox = new ChoiceBox(FXCollections.observableArrayList("MORPH_RECT", "MORPH_ELLIPSE"));
        erodeShapeBox.setValue("MORPH_RECT");

        transformTypeBox =
                new ChoiceBox(FXCollections.observableArrayList("MORPH_OPEN", "MORPH_CLOSE", "MORPH_GRADIENT", "MORPH_TOPHAT", "MORPH_BLACKHAT"));
        transformTypeBox.setValue("MORPH_OPEN");

        erodeShapeBox.addEventHandler(EventType.ROOT, event -> refreshErodeImage());
        transformTypeBox.addEventHandler(EventType.ROOT, event -> refreshErodeImage());
        kSizeField = new TextField("3");
        kSizeField.addEventHandler(EventType.ROOT, e -> refreshErodeImage());
        iterationNumberField = createSlider(0,10);

        originalImageView = new ImageView(createImage(getImageArray(CURRENT_IMAGE)));
        erodedImageView = new ImageView(createImage(erode(getImageArray(CURRENT_IMAGE), 0, 3, 1)));

        VBox imageVbox = new VBox();
        imageVbox.getChildren().addAll(erodedImageView, createHbox(new Label("ksize"), kSizeField),
                createHbox(new Label("iteration"), iterationNumberField),
                createHbox(new Label("shape"), erodeShapeBox),
                createHbox(new Label("transfromType"), transformTypeBox));

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
            refreshErodeImage();
        });
        return slider;
    }

    private void refreshErodeImage(){
        int kSize = Integer.valueOf(kSizeField.getText());
        int iterationNumber = (int)(iterationNumberField.getValue());
        int shape = "MORPH_RECT".equals(erodeShapeBox.getValue()) ? 0 : 1;
        String morphValue = (String) transformTypeBox.getValue();
        int transfromType = 0;
        switch (morphValue){
            case "MORPH_OPEN": transfromType = 0; break;
            case "MORPH_CLOSE":transfromType = 1; break;
            case "MORPH_GRADIENT":transfromType = 2; break;
            case "MORPH_TOPHAT":transfromType = 3; break;
            case "MORPH_BLACKHAT":transfromType = 4; break;
        }
        Mat imageArray = getImageArray(CURRENT_IMAGE);//CannyAlg.execute(CURRENT_IMAGE, 10, 100, 3, true);
        Mat filteredImage = transform(imageArray, transfromType, shape, kSize, iterationNumber);
        erodedImageView.setImage(createImage(filteredImage));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
