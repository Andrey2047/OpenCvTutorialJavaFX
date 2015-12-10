package algorithm;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.Highgui.imread;

/**
 * Created by andriiko on 12/3/2015.
 */
public class BlurFilters {

    public static Mat gausBlur(String imageUrl, int xKernelSize, int yKernelSize, double sigmaX, double sigmaY ){
        Mat img = imread(imageUrl, 1);
        Mat dst = new Mat();
        Imgproc.GaussianBlur(img, dst, new Size(xKernelSize, yKernelSize), sigmaX, sigmaY);
        return dst;
    }

    public static Mat simpleBlur(Mat image){
        Mat dst = new Mat();
        Imgproc.blur(image, dst, new Size(3, 3));
        return dst;
    }

    public static Mat medianBlur(String imageUrl, int kSize){
        Mat img = imread(imageUrl, 1);
        Mat dst = new Mat();
        Imgproc.medianBlur(img, dst, kSize);
        return dst;
    }

    public static Mat bilateralBlur(String imageUrl, double sigmaColor, double sigmaSpace){

        Mat img = imread(imageUrl, 1);
        Mat dst = new Mat();
        Imgproc.bilateralFilter(img, dst, -1, sigmaColor, sigmaSpace);
        return dst;
    }


}
