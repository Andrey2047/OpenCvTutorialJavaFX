package gui;


import algorithm.CommonService;
import algorithm.Histogram;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.opencv.core.Mat;


import static algorithm.CommonService.getImageArray;

/**
 * Created by andriiko on 12/11/2015.
 */
public class HistogramTransformationGUI extends AbstractGUI{

    ImageView equalizedView;
    ImageView grayedView;

    @Override
    public void prepareStart() {
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage = new ImageView(createImage(imageArray));
        grayedView = new ImageView(createImage(CommonService.grayImage(imageArray)));
        equalizedView = new ImageView(createImage(Histogram.equalizeHistogram(imageArray)));
        row1.getChildren().addAll(imageList, originalImage, grayedView);
        row2.getChildren().addAll(equalizedView);
        addRow(row1, row2);
    }

    @Override
    public void refreshAllImages() {
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage.setImage(createImage(imageArray));
        grayedView.setImage(createImage(CommonService.grayImage(imageArray)));
        equalizedView.setImage(createImage(Histogram.equalizeHistogram(imageArray)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
