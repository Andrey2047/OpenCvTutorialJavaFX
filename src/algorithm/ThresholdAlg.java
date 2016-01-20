package algorithm;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samsung on 12/8/2015.
 */
public class ThresholdAlg {

    /**
     *
     * @param image
     * @param thresholdValue
     * @param maxval
     * @param type -
     *             0 - Binary
     *             1 - Binary Inv
     *             2 - Truncate
     *             3 - To zero
     *             4 - To zero inv
     */
    public static Mat threshold(Mat image, double thresholdValue, double maxval, int type){
        Mat dst = new Mat();
        Imgproc.cvtColor(image, dst, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(dst, dst, thresholdValue, maxval, type);
        return dst;
    }

    public static Mat thresholdOneChannel(Mat image, double thresholdValue, double maxval, int type){
        Mat dst = new Mat();
        Imgproc.threshold(image, dst, thresholdValue, maxval, type);
        return dst;
    }

    public static Mat thresholdAndCastToThreeChanel(Mat image, double thresholdValue, double maxval, int type){
        Mat dst = new Mat();
        Imgproc.threshold(image, dst, thresholdValue, maxval, type);
        List<Mat> arrays = new ArrayList<>();
        arrays.add(dst);
        arrays.add(dst);
        arrays.add(dst);
        Core.merge(arrays, dst);
        return dst;
    }

    public static Mat castToThreeChannel(Mat image){
        List<Mat> arrays = new ArrayList<>();
        Mat dst = new Mat();
        arrays.add(image);
        arrays.add(image);
        arrays.add(image);
        Core.merge(arrays, dst);
        return dst;
    }


}
