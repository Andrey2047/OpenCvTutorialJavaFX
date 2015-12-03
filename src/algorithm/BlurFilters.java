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

    public static Mat medianBlur(){
        return new Mat();
    }

    public static Mat bilateralBlur(){
        return new Mat();
    }


}
