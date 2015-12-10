package algorithm;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.*;

import static org.opencv.imgproc.Imgproc.*;

/**
 * Created by andriiko on 11/30/2015.
 */
public class FindContours {

    public static List<MatOfPoint> findContours(Mat image, int mode, int method){
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    public static MatOfInt findConvexHull(MatOfPoint contour){
        MatOfInt chain = new MatOfInt();
        Imgproc.convexHull(contour, chain, true);
        return chain;
    }

    public static List<MatOfPoint> findConvexHull(List<MatOfPoint> contours){
        List<MatOfPoint> toReturn = new ArrayList<>();
        for(MatOfPoint matOfPoint: contours){
            MatOfInt convexHull = findConvexHull(matOfPoint);
            toReturn.add(hull2Points(convexHull, matOfPoint));
        }

        return toReturn;
    }

    public static List<MatOfPoint> findCircles(Mat image, int mode, int method){
        List<MatOfPoint> contours = findContours(image, mode, method);
        List<MatOfPoint> toReturn = new ArrayList<MatOfPoint>();
        for(MatOfPoint matOfPoint : contours){
            double area = Math.abs(Imgproc.contourArea(matOfPoint));
            double length = Imgproc.arcLength(new MatOfPoint2f(matOfPoint.toArray()), true);
            if ( area > 25 && area / (length * length) > 0.07 && area / (length * length)< 0.087 ){ // в 10% интервале
                toReturn.add(matOfPoint);
            }
        }
        return toReturn;
    }

    public static List<MatOfPoint> findTriangles(Mat image, int mode, int method){
        List<MatOfPoint> contours = findContours(image, mode, method);
        List<MatOfPoint> toReturn = new ArrayList<MatOfPoint>();
        for(MatOfPoint matOfPoint : contours){

            if(Math.abs(contourArea(matOfPoint)) < 100){
                continue;
            }

            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f curve = new MatOfPoint2f(matOfPoint.toArray());
            approxPolyDP(curve, approxCurve, arcLength(curve, true)* 0.02, true);

            if(approxCurve.toList().size() == 3){
                toReturn.add(new MatOfPoint(approxCurve.toArray()));
            }
        }
        return toReturn;
    }

    public static Mat plotContours(List<MatOfPoint> contours, Mat image, Scalar color){
        Mat dst = image.clone();
        Imgproc.drawContours(dst, contours, -1, color);
        return dst;
    }

    static MatOfPoint hull2Points(MatOfInt hull, MatOfPoint contour) {
        List<Integer> indexes = hull.toList();
        List<Point> points = new ArrayList<>();
        MatOfPoint point= new MatOfPoint();
        for(Integer index:indexes) {
            points.add(contour.toList().get(index));
        }
        point.fromList(points);
        return point;
    }

    public static Mat plotContours(Map<String, List<MatOfPoint>> contoursMap, Mat image){
        Mat dst = image.clone();

        for(String contourName : contoursMap.keySet()){
            List<MatOfPoint> shapeContours = contoursMap.get(contourName);
            Imgproc.drawContours(dst, shapeContours, -1, new Scalar(0, 0, 255));
            for (MatOfPoint c: shapeContours){
                Moments moments = Imgproc.moments(c);
                int x = (int) (moments.get_m10()/ moments.get_m00());
                int y = (int) (moments.get_m01()/ moments.get_m00());
                Core.putText(dst, contourName, new Point(x,y), Core.FONT_HERSHEY_PLAIN, 1, new Scalar(255, 0, 0));
            }
        }
        return dst;
    }
}
