package video;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Samsung on 12/21/2015.
 */
public class BackgroundCaptor {

    private int accumulatedFramesCount = 0;

    private Mat avarageForeground;
    private Mat diffAvarageForeground;

    private Mat previousFrame;
    private Mat currentFrame;

    private Mat lowForeground;
    private Mat lowForeground1;
    private Mat lowForeground2;
    private Mat lowForeground3;

    private Mat highForeground;
    private Mat highForeground1;
    private Mat highForeground2;
    private Mat highForeground3;


    public BackgroundCaptor(){
    }

    public synchronized void accumulateBackground(Mat currentFrame){
        this.currentFrame = currentFrame;
        if(accumulatedFramesCount > 0) {
            Imgproc.accumulate(currentFrame, avarageForeground);
            Mat deffScratchImage = new Mat();
            Core.absdiff(currentFrame, previousFrame, deffScratchImage);
            Imgproc.accumulate(deffScratchImage, diffAvarageForeground);
        } else {
            initBaseMatrix(currentFrame);
        }
        currentFrame.copyTo(previousFrame);
        accumulatedFramesCount++;
    }

    private void initBaseMatrix(Mat currentFrame) {
        avarageForeground = new Mat(currentFrame.size(), CvType.CV_32FC3);
        diffAvarageForeground = new Mat(currentFrame.size(), CvType.CV_32FC3);

        previousFrame = new Mat(currentFrame.size(), CvType.CV_8UC3);

        lowForeground = new Mat(currentFrame.size(), CvType.CV_8UC3);
        lowForeground1 = new Mat(currentFrame.size(), CvType.CV_8UC1);
        lowForeground2 = new Mat(currentFrame.size(), CvType.CV_8UC1);
        lowForeground3 = new Mat(currentFrame.size(), CvType.CV_8UC1);

        highForeground = new Mat(currentFrame.size(), CvType.CV_8UC3);
        highForeground1 = new Mat(currentFrame.size(), CvType.CV_8UC1);
        highForeground2 = new Mat(currentFrame.size(), CvType.CV_8UC1);
        highForeground3 = new Mat(currentFrame.size(), CvType.CV_8UC1);
    }

    public void normalizeAccumulators(){
        Core.convertScaleAbs(avarageForeground, avarageForeground, 1f/accumulatedFramesCount, 1);
        Core.convertScaleAbs(diffAvarageForeground, diffAvarageForeground, 1f / accumulatedFramesCount, 1);
        Core.add(diffAvarageForeground, new Scalar(1, 1, 1), diffAvarageForeground);

        setHighThreshold();
        setHighLowThreshold();
    }

    private void setHighThreshold() {
        Core.convertScaleAbs(diffAvarageForeground, currentFrame, 7.0, 0);
        Core.add(currentFrame, avarageForeground, highForeground);
        Core.extractChannel(highForeground, highForeground1, 0);
        Core.extractChannel(highForeground, highForeground2, 1);
        Core.extractChannel(highForeground, highForeground3, 2);
    }

    private void setHighLowThreshold() {
        Core.convertScaleAbs(diffAvarageForeground, currentFrame, 6.0, 0);
        Core.subtract(currentFrame, avarageForeground, lowForeground);
        Core.extractChannel(lowForeground, lowForeground1, 0);
        Core.extractChannel(lowForeground, lowForeground2, 1);
        Core.extractChannel(lowForeground, lowForeground3, 2);
    }

    public Mat backgroundDiff(Mat frame){
        int frameWidth = frame.width();
        int frameHeigth = frame.height();
        Mat resultMask = new Mat(frame.size(), CvType.CV_8UC1);

        Mat chanel1 = new Mat(frame.size(), CvType.CV_8UC1);
        Mat chanel2 = new Mat(frame.size(), CvType.CV_8UC1);
        Mat chanel3 = new Mat(frame.size(), CvType.CV_8UC1);

        Core.extractChannel(frame, chanel1, 0);
        Core.extractChannel(frame, chanel2, 1);
        Core.extractChannel(frame, chanel3, 2);

        Mat iMaskT = new Mat(frame.size(), CvType.CV_8UC1, new Scalar(0));

        for(int i=0; i < frameHeigth - 1; i++){
            for(int j=0; j < frameWidth - 1; j++){
                if(chanel1.get(i,j)[0] > lowForeground1.get(i,j)[0] &&
                        chanel1.get(i,j)[0] < highForeground1.get(i,j)[0]     ){
                    resultMask.get(i,j)[0] = 255;
                }
            }
        }

        for(int i=0; i < frameHeigth - 1; i++){
            for(int j=0; j < frameWidth - 1; j++){
                if(chanel2.get(i,j)[0] > lowForeground2.get(i,j)[0] &&
                        chanel2.get(i,j)[0] < highForeground2.get(i,j)[0]     ){
                    iMaskT.get(i,j)[0] = 255;
                }
            }
        }
        Core.bitwise_or(resultMask, iMaskT, resultMask);

        for(int i=0; i < frameHeigth - 1; i++){
            for(int j=0; j < frameWidth - 1; j++){
                if(chanel3.get(i,j)[0] >= lowForeground3.get(i,j)[0] &&
                        chanel2.get(i,j)[0] <= highForeground3.get(i,j)[0]     ){
                    iMaskT.get(i,j)[0] = 255;
                }
            }
        }
        Core.bitwise_or(resultMask, iMaskT, resultMask);

        return resultMask;
    }

    public Mat getLowForeground(){
        return lowForeground;
    }

    public Mat getHighForeground(){
        return lowForeground;
    }

    public int getAccumulator(){
        return accumulatedFramesCount;
    }

    public Mat getAvarageForeground(){
        return avarageForeground;
    }

    public Mat getDiffAvarageForeground(){
        return diffAvarageForeground;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat m = new Mat(30, 40,  CvType.CV_8UC3, new Scalar(11.0, 22.0, 33.0));

        Mat m1 = new Mat(m.size(),  CvType.CV_8UC1);
        Mat m2 = new Mat(m.size(),  CvType.CV_8UC1);
        Mat m3 = new Mat(m.size(),  CvType.CV_8UC1);
        List<Mat> mv = new ArrayList<>();
        mv.add(m1);
        mv.add(m2);
        mv.add(m3);
        Core.extractChannel(m, m2, 1);
        System.out.println(m.channels());
        printMatrix(m2);
        //printMatrix3(m);

        //printMatrix(m2);
        //printMatrix(m3);

//        float alpha = 1f/2f;
//        Core.convertScaleAbs(m1, m2, alpha, 0);
//        for(int i=0; i < 10; i++){
//            for(int j=0; j < 10; j++){
//                System.out.print(m2.get(i,j)[0] + " " +m2.get(i,j)[1] + " "+m2.get(i,j)[2]);
//            }
//            System.out.println();
//        }
    }


    static void printMatrix(Mat mat){
        for(int i=0; i < mat.rows(); i++){
            for(int j=0; j < mat.cols(); j++){
                double c1 = mat.get(i, j)[0];
                if(c1 != 0.0d){
                    System.out.print(c1 + "_1_coord x=" + i +" y=" + j + " ");
                }
            }
            System.out.println();
        }
    }

    static void printMatrix3(Mat mat){
        for(int i=0; i < mat.rows(); i++){
            for(int j=0; j < mat.cols(); j++){
                double c1 = mat.get(i, j)[0];
                double c2 = mat.get(i, j)[1];
                double c3 = mat.get(i, j)[2];
                if(c1 != 0.0d){
                    System.out.print(c1 + "_1_coord x=" + i +" y=" + j + " ");
                }

                if(c2 != 0.0d){
                    System.out.print(c2 + "_2_coord x=" + i + " y=" + j + "");
                }

                if(c3 != 0.0d){
                    System.out.print(c3 + "_3_coord y=" + i +" y=" + j + " ");               }
            }
            System.out.println();
        }
    }

}
