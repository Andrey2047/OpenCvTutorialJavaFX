import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;

/**
 * Created by Samsung on 12/20/2015.
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

    protected void addRow(HBox... rows){
        for (int i = 0; i < rows.length; i++) {
            rows[i].setSpacing(20);
        }
        root.getChildren().addAll(rows);
    }

    public static Image createImage(byte[] mat){
        return new Image(new ByteArrayInputStream(mat));
    }

    protected HBox createHbox(Node... nodes) {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(nodes);
        return hbox;
    }

    protected VBox createVbox(Node... nodes) {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(nodes);
        return vbox;
    }

    protected void populateImageList() {
        imageList = new ComboBox();
        imageList.setItems(FXCollections.observableArrayList(
                Arrays.asList(new File("./" + getImagePath()).listFiles()).
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

    protected Slider createSlider(int min, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.addEventHandler(EventType.ROOT, event -> {
            refreshAllImages();
        });
        return slider;
    }

    protected ScrollPane createScrollPane(ImageView imageView){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(imageView);
        return scrollPane;
    }

    public String getImagePath(){
        return PATH_TO_IMAGES;
    }
}
