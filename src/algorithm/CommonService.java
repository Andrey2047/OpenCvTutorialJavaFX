package algorithm;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * Created by andriiko on 11/24/2015.
 */
public class CommonService {

    public static Mat binarizeImage(Mat image, int lowBorder, int highBorder){
        Mat dst = new Mat();
        Mat gray = new Mat();
        cvtColor(image, gray, Imgproc.COLOR_RGB2GRAY);
        Core.inRange(gray, new Scalar(lowBorder), new Scalar(highBorder), dst);
        return dst;
    }

    public static MatOfByte convertToMatOfByte(Mat mat){
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", mat, matOfByte);
        return matOfByte;
    }

    public static Mat getImageArray(String imgUrl){
        return imread(imgUrl, 1);
    }
}
