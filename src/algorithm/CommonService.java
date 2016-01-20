package algorithm;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * Created by andriiko on 11/24/2015.
 */
public class CommonService {

    public static Mat grayImage(Mat image){
        Mat gray = new Mat();
        cvtColor(image, gray, Imgproc.COLOR_RGB2GRAY);
        return gray;
    }


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

    public static Mat getROI(Mat imagem, int xLower, int yLower, int xUpper, int yUpper){
        return imagem.submat(yLower, yUpper, xLower, xUpper);
    }

    public static Mat convertImagesToHSV(Mat image) {
        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV_FULL);
        return hsvImage;
    }

    public static Mat getHueSatImage(Mat image) {
        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV_FULL);
        ArrayList<Mat> mv = new ArrayList<>();
        mv.add(new Mat(image.size(), CvType.CV_8UC1));
        mv.add(new Mat(image.size(), CvType.CV_8UC1));
        mv.add(new Mat(image.size(), CvType.CV_8UC1));
        Core.split(hsvImage, mv);
        ArrayList<Mat> dst = new ArrayList<>(2);
        dst.add(new Mat(mv.get(1).size(), CvType.CV_8UC1, new Scalar(0)));
        dst.add(new Mat(mv.get(1).size(), CvType.CV_8UC1, new Scalar(0)));
        dst.add(new Mat(mv.get(1).size(), CvType.CV_8UC1));
        Core.mixChannels(mv, dst, new MatOfInt(1, 1));
        Mat dstIm = new Mat();
        Core.merge(dst, dstIm);
        return dstIm;
    }


}
