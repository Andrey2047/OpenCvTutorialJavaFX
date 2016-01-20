package gui;


import algorithm.CommonService;
import algorithm.Histogram;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafxext.RubberBandSelection;
import org.opencv.core.Mat;

import static algorithm.CommonService.getImageArray;

/**
 * Created by andriiko on 12/11/2015.
 */
public class BackProjectionGUI extends AbstractGUI{

    private ImageView hsvImageView;
    private ImageView hueSatHistoImageView;
    private ImageView hueSatImageView;
    private ImageView backProjectionImageView;

    private Button refreshRegionButton;

    private RubberBandSelection rubberBandSelection;

    private Slider binsSlider;

    @Override
    public void prepareStart() {
        populateImageList();
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage = new ImageView(createImage(imageArray));
        Group imageLayer = new Group();
        imageLayer.getChildren().add(originalImage);

        rubberBandSelection = new RubberBandSelection(imageLayer);

        refreshRegionButton = new Button("Refresh");

        backProjectionImageView = new ImageView(createImage(imageArray));
        hueSatHistoImageView = new ImageView();
        hueSatImageView = new ImageView();
        refreshRegionButton.setOnAction(event -> {
            refreshAllImages();
        });
        binsSlider = createSlider(3, 100);
        hsvImageView = new ImageView(createImage(CommonService.convertImagesToHSV(imageArray)));
        addRow(createHbox(imageList, binsSlider, refreshRegionButton));
        addRow(createHbox(imageLayer, hsvImageView, hueSatImageView));
        addRow(createHbox(backProjectionImageView, hueSatHistoImageView));
    }

    @Override
    public void refreshAllImages() {
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        originalImage.setImage(createImage(imageArray));
        Mat img = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        int xLower = (int) rubberBandSelection.getBounds().getMinX();
        int xUpper = (int) rubberBandSelection.getBounds().getMaxX();
        int yLower = (int) rubberBandSelection.getBounds().getMinY();
        int yUpper = (int) rubberBandSelection.getBounds().getMaxY();
        Mat imageCut = CommonService.getROI(img, xLower, yLower, xUpper, yUpper);
        hueSatHistoImageView.setImage(createImage(Histogram.plotHueSaturationHistogram(imageCut, (int) binsSlider.getValue())));
        backProjectionImageView.setImage(createImage(Histogram.backProjection(img, Histogram.getImagesHistogram(imageCut, (int) binsSlider.getValue()))));
        hsvImageView.setImage(createImage(CommonService.convertImagesToHSV(imageArray)));
        hueSatImageView.setImage(createImage(CommonService.getHueSatImage(imageArray)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
