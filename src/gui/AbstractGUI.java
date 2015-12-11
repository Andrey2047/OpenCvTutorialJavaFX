package gui;

import algorithm.CommonService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;

/**
 * Created by andriiko on 11/26/2015.
 */
public abstract class AbstractGUI extends Application {

    VBox root;
    ComboBox imageList;
    ImageView originalImage;

    public static final String PATH_TO_IMAGES = "src/resources/images/";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new VBox();
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);
        populateImageList();
        prepareStart();
        primaryStage.setTitle("Convex Hull Alg");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    void addRow(HBox... rows){
        for (int i = 0; i < rows.length; i++) {
            rows[i].setSpacing(20);
        }
        root.getChildren().addAll(rows);
    }

    public static Image createImage(Mat mat){
        return new Image(new ByteArrayInputStream(CommonService.convertToMatOfByte(mat).toArray()));
    }

    HBox createHbox(Node... nodes) {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(nodes);
        return hbox;
    }

    protected void populateImageList() {
        imageList = new ComboBox();
        imageList.setItems(FXCollections.observableArrayList(
                Arrays.asList(new File("./src/resources/images").listFiles()).
                        stream().
                        filter(File::isFile).
                        map(File::getName).
                        toArray()));
        imageList.setValue("hand2.jpg");
        imageList.addEventHandler(EventType.ROOT, event -> refreshAllImages());
    }

    public abstract void refreshAllImages();

    public void prepareStart() throws Exception{

    }


}
