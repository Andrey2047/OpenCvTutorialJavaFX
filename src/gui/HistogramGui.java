package gui;

import algorithm.Histogram;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.opencv.core.Mat;

import static algorithm.CommonService.getImageArray;

/**
 * Created by Samsung on 12/15/2015.
 */
public class HistogramGui extends AbstractGUI {

    ImageView histogramImage;

    public void prepareStart() {
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage = new ImageView(createImage(imageArray));
        histogramImage = new ImageView(createImage(Histogram.plotHueSaturationHistogram(imageArray)));
        row1.getChildren().addAll(imageList, createScrollPane(originalImage));
        row2.getChildren().addAll(histogramImage);
        addRow(row1, row2);
    }

    @Override
    public void refreshAllImages() {
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage.setImage(createImage(imageArray));
        histogramImage.setImage(createImage(Histogram.plotHueSaturationHistogram(imageArray)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
