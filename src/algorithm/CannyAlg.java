package algorithm;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.Sobel;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by andriiko on 11/25/2015.
 */
public class CannyAlg {

    public static Mat execute(String imageUrl, double threshold1, double threshold2, int apertureSize, boolean L2gradient){
        Mat image = imread(imageUrl, 1);
        Mat grayedImage = image.clone();
        cvtColor(image, grayedImage, Imgproc.COLOR_RGB2GRAY);
        Mat contours = grayedImage.clone();
        Canny(image, contours, threshold1, threshold2, apertureSize, L2gradient);
        Mat diffContours = contours.clone();
        Sobel(contours, diffContours, CvType.CV_16S, 1, 1, 3 , 1, 1);
        return diffContours;
    }
}
