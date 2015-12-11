package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import algorithm.HoughDetector;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

/**
 * Created by andriiko on 11/25/2015.
 */
public class HoughLinesDetectorGUI extends AbstractGUI {

    static String currentImage = "dog";

    static TextField rhoField;
    static TextField thetaField;
    static TextField thresholdField;
    static TextField srnField;
    static TextField stnField;

    static ImageView imageView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        Image img = createImage(HoughDetector.detectLines(PATH_TO_IMAGES + currentImage + ".jpg", 1, Math.PI / 180, 50, 50, 10));

        originalImage = new ImageView(new Image(getClass().getResource("dog.jpg").toURI().toString()));
        imageView = new ImageView(img);

        rhoField = new TextField("1");
        thetaField = new TextField("1");
        thresholdField = new TextField("3");

        srnField = new TextField("1");
        stnField = new TextField("1");

        Button refreshButton = new Button("refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                double rho = Double.valueOf(rhoField.getText());
                double theta = Double.valueOf(thetaField.getText());
                int threshold = Integer.valueOf(thresholdField.getText());
                double srn = Double.valueOf(srnField.getText());
                double stn = Double.valueOf(stnField.getText());


                Image img = createImage(HoughDetector.detectLines(PATH_TO_IMAGES + currentImage + ".jpg", rho, theta, threshold, srn, stn));
                imageView.setImage(img);
            }
        });

        HBox hBox0 = new HBox();
        HBox hBox1 = new HBox();
        HBox hBox3 = new HBox();
        HBox hBox4 = new HBox();
        HBox hBox5 = new HBox();

        HBox hBox6 = new HBox();

        final ToggleGroup group = new ToggleGroup();
        ToggleButton tb1 = new ToggleButton("Lines");
        tb1.setToggleGroup(group);
        tb1.setSelected(true);

        ToggleButton tb2 = new ToggleButton("Circles");
        tb1.setToggleGroup(group);


        hBox0.getChildren().addAll(originalImage, imageView );
        hBox1.getChildren().addAll(new Label("rhoField"), rhoField, new Label("thetaField"), thetaField);
        hBox3.getChildren().addAll(new Label("threshold"), thresholdField, new Label("srn"), srnField);
        hBox4.getChildren().addAll(new Label("stn"), stnField, refreshButton);
        hBox5.getChildren().addAll(createImageButton("dog"), createImageButton("hand"), createImageButton("car"), createImageButton("triangle"));
        hBox6.getChildren().addAll(tb1, tb2);

        root.getChildren().addAll(hBox0, hBox1, hBox3, hBox4, hBox5, hBox6);

        primaryStage.setTitle("Hough Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private Button createImageButton(final String imageName){
        Button button = new Button(imageName);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentImage = imageName;
                Image img = createImage(HoughDetector.detectLines(PATH_TO_IMAGES + currentImage + ".jpg", 1, 1, 1, 1, 1));
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


