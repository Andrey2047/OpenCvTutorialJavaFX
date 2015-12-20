package algorithm;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by andriiko on 12/11/2015.
 */
public class Histogram {

    public static final MatOfFloat RANGES = new MatOfFloat(0, 255, 0, 255);

    /**
     * Makes image more contrast.
     */
    public static Mat equalizeHistogram(Mat image){
        Mat dst = new Mat();
        //cvtColor(image, dst, Imgproc.COLOR_RGB2GRAY);
        Imgproc.equalizeHist(image, dst);
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

    public static Mat getImagesHistogram(List<Mat> images, int beans){
        Mat mMaskMat = new Mat();
        Mat histogramMat = new Mat();
        MatOfInt histSize = new MatOfInt(beans, beans+2);

        Imgproc.calcHist(convertImagesToHSV(images), new MatOfInt(0, 1), mMaskMat, histogramMat, histSize, RANGES);

        Mat nHistogramMat = new Mat();
        Core.normalize(histogramMat, nHistogramMat, 0, 255, Core.NORM_MINMAX, -1, new Mat());

        return histogramMat;
    }

    private static  List<Mat>  convertImagesToHSV(List<Mat> images) {
        List<Mat> huvImageArr = new ArrayList<>();
        for(Mat image: images){
            Mat hsvImage = new Mat();
            cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV_FULL);

            huvImageArr.add(hsvImage);
        }

        return huvImageArr;
    }

    public static Mat backProjection(Mat image, Mat hueSatHistogram){
        Mat backProjection = new Mat();

        Imgproc.calcBackProject(Arrays.asList(image), new MatOfInt(0, 1), hueSatHistogram, backProjection, new MatOfFloat(0, 180, 0, 255), 1);

        return backProjection;
    }

    public static Mat backProjectionPatch(Mat image, Mat hueSatHistogram){
        Mat backProjection = new Mat();

        Imgproc.calcBackProject(Arrays.asList(image), new MatOfInt(0, 1), hueSatHistogram, backProjection, new MatOfFloat(0, 180, 0, 255), 1);

        return backProjection;
    }


    public static Mat plotHueSaturationHistogram(Mat image){
        Mat histogram = getImagesHistogram(Arrays.asList(image), 30);
        return buildImageHistogram(histogram, 30, 30);
    }

    public static Mat plotHueSaturationHistogram(Mat image, int beansCount){
        Mat histogram = getImagesHistogram(Arrays.asList(image), beansCount);
        return buildImageHistogram(histogram, beansCount, beansCount+2);
    }

    public static Mat buildImageHistogram(Mat histogram, int hBins, int sBins){
        int scale = 5;
        Mat dst = new Mat(new Size(hBins*scale, sBins*scale), CvType.CV_8UC3);
        for(int hue=0; hue < hBins; hue++){
            for(int saturation =0; saturation < sBins; saturation++){
                double binValue = histogram.get(hue, saturation)[0];
                Point lowPoint = new Point((hue * scale), saturation * scale);
                Point upperPoint = new Point((hue+1) * scale-1, (saturation+1) * scale -1);
                Core.rectangle(dst, lowPoint, upperPoint, new Scalar(binValue*scale, binValue*scale, binValue*scale), Core.FILLED);
            }
        }
        return dst;
    }

}
