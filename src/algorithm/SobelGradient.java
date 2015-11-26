package algorithm;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.Laplacian;
import static org.opencv.imgproc.Imgproc.Sobel;

/**
 * Created by andriiko on 11/24/2015.
 */
public class SobelGradient {

    public static Mat execute(String imgUrl, int xOrder, int yOrder, int kSize, double delta, double scale, int lowBrightness, int highBrightness){
        Mat image = imread(imgUrl, 1);//CommonService.binarizeImage(imgUrl, lowBrightness, highBrightness);
        Mat dst = image.clone();
        Sobel(image, dst, CvType.CV_16S, xOrder, yOrder, kSize, delta, scale);
        //Laplacian(image, dst, CvType.CV_32F, kSize, delta, scale);
        Mat dst21 = dst.clone();
        convertScaleAbs(dst, dst21);
        return dst21;
    }

}
