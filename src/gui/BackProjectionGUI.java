package gui;


import algorithm.CommonService;
import algorithm.Histogram;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.opencv.core.Mat;

import static algorithm.CommonService.getImageArray;

/**
 * Created by andriiko on 12/11/2015.
 */
public class BackProjectionGUI extends AbstractGUI{

    ImageView equalizedView;
    ImageView grayedView;

    Slider binsSlider;

    @Override
    public void prepareStart() {
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage = new ImageView(createImage(imageArray));
        grayedView = new ImageView(createImage(CommonService.grayImage(imageArray)));
        equalizedView = new ImageView(createImage(Histogram.selfBackProjection(imageArray, 10)));
        row1.getChildren().addAll(imageList, originalImage, grayedView);

        binsSlider = createSlider(25, 50);

        row2.getChildren().addAll(createVbox(equalizedView,
                createHbox(new Label("beans"), binsSlider)));
        addRow(row1, row2);
    }

    @Override
    public void refreshAllImages() {
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage.setImage(createImage(imageArray));
        grayedView.setImage(createImage(CommonService.grayImage(imageArray)));
        int beans = (int)binsSlider.getValue();
        equalizedView.setImage(createImage(Histogram.selfBackProjection(imageArray, beans)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
