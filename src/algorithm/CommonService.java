package algorithm;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * Created by andriiko on 11/24/2015.
 */
public class CommonService {

    public static Mat binarizeImage(String imagePath, int lowBorder, int highBorder){
        Mat image = imread(imagePath, 1);
        Mat imageGray = image.clone();
        Mat dst = image.clone();

        cvtColor(image, imageGray, Imgproc.COLOR_RGB2GRAY);
        threshold(imageGray, dst, lowBorder, highBorder, Imgproc.THRESH_BINARY);

        return dst;
    }

    public static MatOfByte convertToMatOfByte(Mat mat){
        MatOfByte dst = new MatOfByte();
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", dst, matOfByte);
        return matOfByte;
    }
}
