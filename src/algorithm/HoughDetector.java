package algorithm;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.HoughLinesP;

/**
 * Created by andriiko on 11/25/2015.
 */
public class HoughDetector {

    public static Mat detectLines(String imageUrl, double rho, double theta, int threshold, double srn, double stn){
        Mat image = imread(imageUrl, 1);//CommonService.binarizeImage(imgUrl, lowBrightness, highBrightness);
        Mat dst = image.clone();
        Mat grayedImage = new Mat();
        Imgproc.cvtColor(image, grayedImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(grayedImage, grayedImage, new Size(3, 3));

        Mat edges = new Mat();
        int lowThreshold = 50;
        int ratio = 3;
        Imgproc.Canny(grayedImage, edges, lowThreshold, lowThreshold * ratio);

        Mat resultLines = new Mat();

        HoughLinesP(edges, resultLines, rho, theta, threshold, srn, stn);

        for(int i = 0; i < resultLines.cols(); i++) {
            double[] val = resultLines.get(0, i);
            Point pt1 = new Point(val[0], val[1]);
            Point pt2 = new Point(val[2], val[3]);
            Core.line(dst, pt1, pt2, new Scalar(0, 0, 255), 2);
        }

        return dst;
    }

    public static Mat detectCircles(String imageUrl, double dp, double minDist, double param1, double param2, int minR, int maxR){
        Mat image = imread(imageUrl, 1);
        Mat dst = image.clone();
        Mat grayedImage = new Mat();
        Imgproc.cvtColor(image, grayedImage, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(grayedImage, grayedImage, new Size(9, 9),2 ,2);

//        Mat edges = new Mat();
//        int lowThreshold = 50;
//        int ratio = 3;
//        Imgproc.Canny(grayedImage, edges, lowThreshold, lowThreshold * ratio);

        Mat circles = new Mat();

        Imgproc.HoughCircles(grayedImage, circles, Imgproc.CV_HOUGH_GRADIENT, dp, minDist, param1, param2, minR, maxR);

        for(int i = 0; i < circles.cols(); i++) {
            double[] val = circles.get(0, i);
            Point pt1 = new Point(val[0], val[1]);
            int radius = (int) val[2];
            Core.circle(dst, pt1, radius, new Scalar(0, 0, 255), 1, 8, 0);
        }

        return dst;
    }

}
