package algorithm;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


/**
 * Created by Samsung on 12/8/2015.
 *
 * When ksize is odd the image will be shifted after each iteration.
 * Black Hat transformation can distinguish image contours.
 *
 *
 */
public class MorphologicalTransformationFunction {


    public static Mat erode(Mat image, int shape, int kSize, int iteration){
        Mat dst = new Mat();
        Imgproc.erode(image, dst, Imgproc.getStructuringElement(shape, new Size(kSize, kSize)), new Point(-1, -1), iteration);
        return dst;
    }

    public static Mat dialate(Mat image, int shape, int kSize, int iteration){
        Mat dst = new Mat();
        Imgproc.dilate(image, dst, Imgproc.getStructuringElement(shape, new Size(kSize, kSize)), new Point(-1, -1), iteration);
        return dst;
    }

    public static Mat transform(Mat image, int transformType, int shape, int kSize, int iteration){
        Mat dst = new Mat();
        Imgproc.morphologyEx(image, dst, transformType, Imgproc.getStructuringElement(shape, new Size(kSize, kSize)), new Point(-1, -1), iteration);
        return dst;
    }
}
