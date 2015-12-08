package algorithm;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

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
}
