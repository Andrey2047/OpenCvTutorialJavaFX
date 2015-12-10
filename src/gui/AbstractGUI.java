package gui;

import algorithm.CommonService;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.ByteArrayInputStream;

/**
 * Created by andriiko on 11/26/2015.
 */
public abstract class AbstractGUI extends Application {

    public static final String PATH_TO_IMAGES = "src/resources/images/";
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Image createImage(Mat mat){
        return new Image(new ByteArrayInputStream(CommonService.convertToMatOfByte(mat).toArray()));
    }

    HBox createHbox(Node... nodes) {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(nodes);
        return hbox;
    }


}
