package sample;

import javafx.application.Application;
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
import sample.opencvsample.HoughLinesDetector;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

/**
 * Created by andriiko on 11/25/2015.
 */
public class HoughLinesDetectorGUI extends Application {

    static String currentImage = "dog";

    static TextField rhoField;
    static TextField thetaField;
    static TextField thresholdField;
    static TextField srnField;
    static TextField stnField;

    static ImageView imageView;
    static ImageView imageViewOrigin;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);

        Image img = new Image(new ByteArrayInputStream(HoughLinesDetector.execute("src/sample/" + currentImage + ".jpg", 1, Math.PI / 180, 50, 50, 10).toArray()));

        imageViewOrigin = new ImageView(new Image(getClass().getResource("dog.jpg").toURI().toString()));
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


                Image img = new Image(new ByteArrayInputStream(HoughLinesDetector.execute("src/sample/" + currentImage + ".jpg", rho, theta, threshold, srn, stn).toArray()));
                imageView.setImage(img);
            }
        });

        HBox hBox0 = new HBox();
        HBox hBox1 = new HBox();
        HBox hBox3 = new HBox();
        HBox hBox4 = new HBox();
        HBox hBox5 = new HBox();

        hBox0.getChildren().addAll(imageViewOrigin, imageView );
        hBox1.getChildren().addAll(new Label("rhoField"), rhoField, new Label("thetaField"), thetaField);
        hBox3.getChildren().addAll(new Label("threshold"), thresholdField, new Label("srn"), srnField);
        hBox4.getChildren().addAll(new Label("stn"), stnField, refreshButton);
        hBox5.getChildren().addAll(createImageButton("dog"), createImageButton("hand"), createImageButton("car"));

        root.getChildren().addAll(hBox0, hBox1, hBox3, hBox4, hBox5);

        primaryStage.setTitle("Hough Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private static Button createImageButton(final String imageName){
        Button button = new Button(imageName);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                currentImage = imageName;
                Image img = new Image(new ByteArrayInputStream(HoughLinesDetector.execute("src/sample/" + currentImage + ".jpg", 1, 1, 1, 1, 1).toArray()));
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


