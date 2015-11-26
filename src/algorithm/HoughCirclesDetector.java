package algorithm;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.HoughLinesP;

/**
 * Created by andriiko on 11/25/2015.
 */
public class HoughCirclesDetector {

    public static Mat execute(String imageUrl, double rho, double theta, int threshold, double srn, double stn){
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

            //double rhoV = val[0];
            //double thetaV = val[1];
            Point pt1 = new Point(val[0], val[1]);
            Point pt2 = new Point(val[2], val[3]);
           // double a = cos(thetaV), b = sin(thetaV);
            //double x0 = a*rhoV, y0 = b*rhoV;
//            pt1.x = round(x0 + 10 * (-b));
//            pt1.y = round(y0 + 10 * (a));
//            pt2.x = round(x0 - 10 * (-b));
//            pt2.y = round(y0 - 10 * (a));
              Core.line(dst, pt1, pt2, new Scalar(0, 0, 255), 2);
        }

        return dst;
    }

    private static double pointDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y), 2));
    }
}
