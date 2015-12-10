package gui;

import algorithm.CommonService;
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
import org.opencv.core.Core;
import algorithm.CannyAlg;
import algorithm.SobelGradient;

import java.net.URISyntaxException;

/**
 * Created by andriiko on 11/25/2015.
 */
public class CannyAlgGui extends AbstractGUI {

    static String currentImage = "dog";

    static TextField threshold1;
    static TextField threshold2;

    static TextField appertureSize;

    static ImageView imageView;
    static ImageView imageViewOrigin;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        Image img = createImage(SobelGradient.execute(PATH_TO_IMAGES + currentImage + ".jpg", 0, 1, 1, 1, 1, 50, 255));

        imageViewOrigin = new ImageView(createImage(CommonService.getImageArray(PATH_TO_IMAGES + currentImage + ".jpg")));
        imageView = new ImageView(img);

        threshold1 = new TextField("1");
        threshold2 = new TextField("1");

        appertureSize = new TextField("3");

        Button refreshButton = new Button("refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                double th1 = Double.valueOf(threshold1.getText());
                double th2 = Double.valueOf(threshold2.getText());
                int apperture = Integer.valueOf(appertureSize.getText());

                Image img = createImage(CannyAlg.execute(PATH_TO_IMAGES + currentImage + ".jpg", th1, th2, apperture, true));
                imageView.setImage(img);
            }
        });

        HBox hBox0 = new HBox();
        HBox hBox1 = new HBox();
        HBox hBox3 = new HBox();

        HBox hBox5 = new HBox();

        hBox0.getChildren().addAll(imageViewOrigin, imageView );
        hBox1.getChildren().addAll(new Label("threshold1"), threshold1, new Label("threshold2"), threshold2);
        hBox3.getChildren().addAll(new Label("appertureSize"), appertureSize, refreshButton);
        hBox5.getChildren().addAll(createImageButton("house"), createImageButton("hand"), createImageButton("car"));

        root.getChildren().addAll(hBox0, hBox1, hBox3, hBox5);

        primaryStage.setTitle("Canny Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private static Button createImageButton(final String imageName){
        Button button = new Button(imageName);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentImage = imageName;
                Image img = createImage(CannyAlg.execute(PATH_TO_IMAGES + currentImage + ".jpg", 1, 1, 3, true));
                imageView.setImage(img);
                try {
                    imageViewOrigin.setImage(new Image(getClass().getResource(imageName + ".jpg").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        return button;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
