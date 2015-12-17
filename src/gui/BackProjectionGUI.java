package gui;


import algorithm.Histogram;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static algorithm.CommonService.getImageArray;
import static algorithm.Histogram.*;
import static algorithm.ThresholdAlg.thresholdAndCastToThreeChanel;

/**
 * Created by andriiko on 12/11/2015.
 */
public class BackProjectionGUI extends AbstractGUI{

    public static final String PATH_TO_IMAGES = "src/resources/images/";
    ComboBox etalonImageList;

    ImageView etalonImage;
    ImageView etalonHistogramImage;

    ImageView originalHistogramImage;

    ImageView backProjectionImage;
    ImageView backProjectionImageHistogramm;

    Mat etalonHistogramm;

    Slider binsSlider;
    Slider tSlider;

    @Override
    public void prepareStart() {
        populateEtalonImageList();
        binsSlider = createSlider(10, 50);
        tSlider = createSlider(0, 255);

        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        Mat etalonImageArray = getImageArray(PATH_TO_IMAGES + etalonImageList.getValue().toString());

        originalImage = new ImageView(createImage(imageArray));
        etalonImage = new ImageView(createImage(etalonImageArray));
        int beanValue = (int) binsSlider.getValue();
        etalonHistogramImage = new ImageView(createImage(plotHueSaturationHistogram(etalonImageArray, beanValue)));
        originalHistogramImage = new ImageView(createImage(plotHueSaturationHistogram(imageArray, beanValue)));

        etalonHistogramm = getImagesHistogram(getHandsImages(), beanValue);
        Mat backProjectionImageArray = Histogram.backProjection(imageArray, etalonHistogramm);

        backProjectionImage = new ImageView(createImage(backProjectionImageArray));
        //backProjectionImageHistogramm = new ImageView(createImage(plotHueSaturationHistogram(backProjectionImageArray, beanValue)));

        addRow(createHbox(imageList, createScrollPane(originalImage), originalHistogramImage));
        addRow(createHbox(etalonImageList, etalonImage, etalonHistogramImage));
        addRow(createHbox(binsSlider, tSlider, createScrollPane(backProjectionImage)));

    }

    protected void populateEtalonImageList() {
        etalonImageList = new ComboBox();
        etalonImageList.setItems(FXCollections.observableArrayList(
                Arrays.asList(new File("./" + getImagePath()).listFiles()).
                        stream().
                        filter(File::isFile).
                        map(File::getName).
                        toArray()));
        etalonImageList.setValue("hand2.jpg");
        etalonImageList.addEventHandler(EventType.ROOT, event -> refreshAllImages());
    }


    @Override
    public void refreshAllImages() {
        Mat imageArray = getImageArray(PATH_TO_IMAGES + imageList.getValue().toString());
        Mat etalonImageArray = getImageArray(PATH_TO_IMAGES + etalonImageList.getValue().toString());

        originalImage.setImage(createImage(imageArray));
        etalonImage.setImage(createImage(etalonImageArray));
        int beanValue = (int) binsSlider.getValue();
        originalHistogramImage.setImage(createImage(plotHueSaturationHistogram(etalonImageArray, beanValue)));
        etalonHistogramm = getImagesHistogram(getHandsImages(), beanValue);
        etalonHistogramImage.setImage(createImage(buildImageHistogram(etalonHistogramm, beanValue, beanValue)));


        double tValue = tSlider.getValue();
        Mat backProjectionImageArray = Histogram.backProjection(imageArray, etalonHistogramm);

        backProjectionImage.setImage(createImage(getBackProjectImage(tValue, backProjectionImageArray, imageArray)));
        //backProjectionImageHistogramm.setImage(createImage(plotHueSaturationHistogram(backProjectionImageArray, beanValue)));
    }

    private Mat getBackProjectImage(double tValue, Mat backProjectionImageArray, Mat originalImage) {
        return thresholdAndCastToThreeChanel(equalizeHistogram(backProjectionImageArray), tValue, 255, 0);
    }

    public List<Mat> getHandsImages(){
        List<Mat> toReturn = new ArrayList<>();
        for(Object fileName: Arrays.asList(new File("./" + getImagePath()+"hands/").listFiles()).
                stream().
                filter(File::isFile).
                map(File::getName).
                toArray()){
            toReturn.add(getImageArray(getImagePath()+"hands/" + fileName.toString()));
        }
        return toReturn;
    }

    @Override
    public String getImagePath() {
        return PATH_TO_IMAGES;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
