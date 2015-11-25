package sample.opencvsample;

import org.bytedeco.javacpp.FloatPointer;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.imgproc.Imgproc.filter2D;

/**
 * Created by andriiko on 11/24/2015.
 */
public class Convolution {

    public static final float [] CONVOLUTION_KERNEL = {0,0,0, 0,1,0, 0,0,0};

    public static final float SMOOTH_COEFF = 0.2f;

    public static final float [] CONVOLUTION_KERNEL_SMOOTHING =
                    {SMOOTH_COEFF,SMOOTH_COEFF,SMOOTH_COEFF,
                     SMOOTH_COEFF, SMOOTH_COEFF, SMOOTH_COEFF,
                     SMOOTH_COEFF,SMOOTH_COEFF,SMOOTH_COEFF};

    public static final float [] CONVOLUTION_KERNEL_CLARITY =
            {-0.1f, -0.1f, -0.1f,
             -0.1f, 2, -0.1f,
             -0.1f, -0.1f, -0.1f,};

    public static final float [] CONVOLUTION_KERNEL_BRIGHTNESS =
                    {-0.1f, 0.2f, -0.1f,
                     0.2f, 3, 0.2f,
                     -0.1f, 0.2f, -0.1f};

    public static final float [] CONVOLUTION_KERNEL_ECLIPSE =
            {-0.1f, 0.1f, -0.1f,
                    0.1f, 0.5f, 0.1f,
                    -0.1f, 0.1f, -0.1f,};

    public static MatOfByte filterImage(String imagePath, float [] cKernel){
        Mat image = imread(imagePath, 1);
        Mat dst = image.clone();
        Mat mat = new MatOfFloat(cKernel);

        filter2D(image, dst, CvType.CV_32F , mat);
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", dst, matOfByte);
        return matOfByte;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        filterImage("C:/edu/courses/image recognition/test_data/dog.jpg", CONVOLUTION_KERNEL);
    }
}
