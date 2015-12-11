package gui;

import algorithm.SobelGradient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URISyntaxException;

public class SobelGradientGui extends AbstractGUI {

    static String currentImage = "dog";

    static TextField lowBrightness;
    static TextField highBrightness;

    static TextField xOrderField;
    static TextField yOrderField;

    static TextField scaleField;
    static TextField deltaField;
    static TextField kSizeField;
    static ImageView imageView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        Image img = createImage(SobelGradient.execute(PATH_TO_IMAGES + currentImage + ".jpg", 0, 1, 1, 1, 1, 50, 255));

        originalImage = new ImageView(new Image(getClass().getResource("dog.jpg").toURI().toString()));
        imageView = new ImageView(img);

        lowBrightness = new TextField("70");
        highBrightness = new TextField("255");
        xOrderField = new TextField("1");
        yOrderField = new TextField("1");
        scaleField = new TextField("1");
        deltaField = new TextField("1");
        kSizeField = new TextField("1");

        Button refreshButton = new Button("refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int lBr = Integer.valueOf(lowBrightness.getText());
                int hBr = Integer.valueOf(highBrightness.getText());
                int xOrder = Integer.valueOf(xOrderField.getText());
                int yOrder = Integer.valueOf(yOrderField.getText());
                int kSize = Integer.valueOf(kSizeField.getText());
                double delta = Double.valueOf(deltaField.getText());
                double scale = Double.valueOf(scaleField.getText());

                Image img = createImage(SobelGradient.execute(PATH_TO_IMAGES + currentImage +".jpg", xOrder, yOrder, kSize, delta, scale, lBr, hBr));
                imageView.setImage(img);
            }
        });

        HBox hBox0 = new HBox();
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();
        HBox hBox4 = new HBox();

        HBox hBox5 = new HBox();

        hBox0.getChildren().addAll(originalImage, imageView );
        hBox1.getChildren().addAll(new Label("xOrder"), xOrderField, new Label("yOrder"), yOrderField);
        hBox2.getChildren().addAll(new Label("lowBriLimit"), lowBrightness, new Label("hignBriLimit"), highBrightness);
        hBox3.getChildren().addAll(new Label("scale"), scaleField, new Label("delta"), deltaField);
        hBox4.getChildren().addAll(new Label("ksize"), kSizeField, refreshButton);
        hBox5.getChildren().addAll(createImageButton("dog"), createImageButton("hand"), createImageButton("triangle"));

        root.getChildren().addAll(hBox0, hBox1, hBox2, hBox3, hBox4, hBox5);

        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private Button createImageButton(final String imageName){
        Button button = new Button(imageName);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentImage = imageName;
                Image img = createImage(SobelGradient.execute(PATH_TO_IMAGES + imageName + ".jpg", 0, 1, 1, 1, 1, 50, 255));
                imageView.setImage(img);
                try {
                    originalImage.setImage(new Image(getClass().getResource(imageName + ".jpg").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        return button;
    }

    @Override
    public void refreshAllImages() {

    }
}
