package video;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/**
 * Created by Samsung on 12/21/2015.
 */
public class BackgroundCaptor {

    private int accumulatedFramesCount = 0;

    private Mat avarageForeground = new Mat();;
    private Mat diffAvarageForeground = new Mat();;
    private Mat previousFrame = new Mat();
    private Mat currentFrame = new Mat();

    private Mat lowForeground = new Mat();
    private Mat lowForeground1 = new Mat();
    private Mat lowForeground2 = new Mat();
    private Mat lowForeground3 = new Mat();

    private Mat highForeground = new Mat();
    private Mat highForeground1 = new Mat();
    private Mat highForeground2 = new Mat();
    private Mat highForeground3 = new Mat();


    public BackgroundCaptor(){
        avarageForeground = new Mat();
        diffAvarageForeground = new Mat();
    }

    public synchronized void accumulateBackground(Mat currentFrame){
        this.currentFrame = currentFrame;
        if(accumulatedFramesCount > 0) {
            Imgproc.accumulate(currentFrame, avarageForeground);
            Mat deffScratchImage = new Mat();
            Core.absdiff(currentFrame, previousFrame, deffScratchImage);
            Imgproc.accumulate(deffScratchImage, diffAvarageForeground);
        }
        currentFrame.copyTo(previousFrame);
        accumulatedFramesCount++;
    }

    public void normalizeAccumulators(){
        Core.convertScaleAbs(avarageForeground, avarageForeground, 1/accumulatedFramesCount, 0);
        Core.convertScaleAbs(diffAvarageForeground, diffAvarageForeground, 1 / accumulatedFramesCount, 0);
        Core.add(diffAvarageForeground, new Scalar(1, 1, 1), diffAvarageForeground);

        setHighThreshold();
        setHighLowThreshold();
    }

    private void setHighThreshold() {
        Core.convertScaleAbs(diffAvarageForeground, currentFrame, 7.0, 0);
        Core.add(currentFrame, avarageForeground, highForeground);
        Core.split(highForeground, Arrays.asList(highForeground1, highForeground2, highForeground3));
    }

    private void setHighLowThreshold() {
        Core.convertScaleAbs(diffAvarageForeground, currentFrame, 6.0, 0);
        Core.subtract(currentFrame, avarageForeground, lowForeground);
        Core.split(lowForeground, Arrays.asList(lowForeground1, lowForeground2, lowForeground3));
    }

//    public Mat backgroundDiff(Mat frame){
//        Mat resultMask = new Mat();
//
//        Mat chanel1 = new Mat();
//        Mat chanel2 = new Mat();
//        Mat chanel3 = new Mat();
//
//        Core.split(frame, Arrays.asList(chanel1, chanel2, chanel3));
//        Core.inRange();
//    }

    public Mat getLowForeground(){
        return lowForeground;
    }

    public Mat getHighForeground(){
        return lowForeground;
    }

    public int getAccumulator(){
        return accumulatedFramesCount;
    }
}
