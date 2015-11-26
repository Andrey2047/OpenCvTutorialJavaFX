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
public class HoughCirclesDetectorGUI extends AbstractGUI {

    static String currentImage = "dog";

    static TextField dpField;
    static TextField minDistField;
    static TextField param1Field;
    static TextField param2Field;
    static TextField minRadiusField;
    static TextField maxRadiusField;

    static ImageView imageView;
    static ImageView imageViewOrigin;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        Image img = createImage(HoughDetector.detectCircles(PATH_TO_IMAGES + currentImage + ".jpg", 1, 1, 1, 2, 1, 2));

        imageViewOrigin = new ImageView(new Image(getClass().getResource("dog.jpg").toURI().toString()));
        imageView = new ImageView(img);

        dpField = new TextField("1");
        minDistField = new TextField("1");
        param1Field = new TextField("3");

        param2Field = new TextField("1");
        minRadiusField = new TextField("1");
        maxRadiusField = new TextField("1");

        Button refreshButton = new Button("refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                double rho = Double.valueOf(dpField.getText());
                double minDist = Double.valueOf(minDistField.getText());
                double thresholdL = Integer.valueOf(param1Field.getText());
                double thresholdH = Integer.valueOf(param2Field.getText());
                int minR = Integer.valueOf(minRadiusField.getText());
                int maxR = Integer.valueOf(maxRadiusField.getText());
                Image img = createImage(HoughDetector.detectCircles(PATH_TO_IMAGES + currentImage + ".jpg", rho, minDist, thresholdL, thresholdH, minR, maxR));
                imageView.setImage(img);
            }
        });

        HBox hBox0 = new HBox();
        HBox hBox1 = new HBox();
        HBox hBox3 = new HBox();
        HBox hBox4 = new HBox();
        HBox hBox5 = new HBox();


        hBox0.getChildren().addAll(imageViewOrigin, imageView );
        hBox1.getChildren().addAll(new Label("dpField"), dpField, new Label("minDistField"), minDistField);
        hBox3.getChildren().addAll(new Label("param1"), param1Field, new Label("param2"), param2Field);
        hBox4.getChildren().addAll(new Label("minRadius"), minRadiusField, new Label("maxRadius"), maxRadiusField, refreshButton);
        hBox5.getChildren().addAll(createImageButton("circles"), createImageButton("circles2"), createImageButton("circles3"));

        root.getChildren().addAll(hBox0, hBox1, hBox3, hBox4, hBox5);

        primaryStage.setTitle("Hough Circles Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private static Button createImageButton(final String imageName){
        Button button = new Button(imageName);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentImage = imageName;
                Image img = createImage(HoughDetector.detectCircles(PATH_TO_IMAGES + currentImage + ".jpg", 1, 1, 1, 2, 1, 2));
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

}


