package algorithm;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by andriiko on 12/11/2015.
 */
public class Histogram {

    public static final MatOfFloat RANGES = new MatOfFloat(0, 180, 0, 255);

    /**
     * Makes image more contrast.
     */
    public static Mat equalizeHistogram(Mat image){
        Mat dst = new Mat();
        cvtColor(image, dst, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(dst, dst);
        return dst;
    }

    public static Mat selfBackProjection(Mat image, int beans){
        List<Mat> huvImageArr = getHueChanelImage(image);
        Mat mMaskMat = new Mat();
        Mat histogramMat = new Mat();
        Imgproc.calcHist(huvImageArr, new MatOfInt(0), mMaskMat, histogramMat, new MatOfInt(beans), new MatOfFloat(0, 108));

        Core.normalize(histogramMat, histogramMat, 0, 255, Core.NORM_MINMAX, -1, new Mat());

        Mat backProjection = new Mat();
        Imgproc.calcBackProject(huvImageArr, new MatOfInt(0), histogramMat, backProjection, new MatOfFloat(0f, 255f), 1);

        return backProjection;
    }

    private static List<Mat> getHueChanelImage(Mat image) {
        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Mat huvImage = new Mat(hsvImage.size(), CvType.CV_8UC2);

        List<Mat> hsvImageArr = new ArrayList<>();
        hsvImageArr.add(0, hsvImage);

        List<Mat> huvImageArr = new ArrayList<>();
        huvImageArr.add(0, huvImage);

        Core.mixChannels(hsvImageArr, huvImageArr, new MatOfInt(0, 0, 1, 1));
        return huvImageArr;
    }

    public static Mat getImageHistogram(Mat image, int beans, float lowRange, float highRange){
        List<Mat> huvImageArr = new ArrayList<>();

        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV_FULL);

        huvImageArr.add(0, hsvImage);

        Mat mMaskMat = new Mat();
        Mat histogramMat = new Mat();
        MatOfInt histSize = new MatOfInt(beans, beans);

        Imgproc.calcHist(huvImageArr, new MatOfInt(0, 1), mMaskMat, histogramMat, histSize, RANGES);

        Core.normalize(histogramMat, histogramMat, 0, 255, Core.NORM_MINMAX, -1, new Mat());
        return histogramMat;
    }

    public static Mat backProjection(Mat image, Mat hueHistogram){
        Mat backProjection = new Mat();

        Imgproc.calcBackProject(Arrays.asList(image), new MatOfInt(0, 1), hueHistogram, backProjection, new MatOfFloat(0, 180, 0, 255), 1);

        return backProjection;
    }


    public static Mat plotImageHistogram(Mat image){
        List<Mat> matList = new LinkedList<Mat>();
        matList.add(image);
        Mat histogram = new Mat();
        MatOfFloat ranges = new MatOfFloat(0,256);
        MatOfInt histSize = new MatOfInt(255);
        Imgproc.calcHist(matList, new MatOfInt(0), new Mat(), histogram, histSize, ranges);
        Mat histImage = Mat.zeros( 100, (int)histSize.get(0, 0)[0], CvType.CV_8UC1);
        Core.normalize(histogram, histogram, 1, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        for( int i = 0; i < (int)histSize.get(0, 0)[0]; i++ ) {
            Core.line(histImage, new Point(i, histImage.rows()), new Point(i, histImage.rows()-Math.round( histogram.get(i,0)[0] )), new Scalar(255, 255, 255), 1, 8, 0);
        }

        return histImage;
    }

}
