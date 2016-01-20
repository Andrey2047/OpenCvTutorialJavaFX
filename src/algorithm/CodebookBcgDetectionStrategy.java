package algorithm;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Created by Samsung on 12/28/2015.
 */
public class CodebookBcgDetectionStrategy {

    private Codebook[][] codebooks;
    private boolean initialized;


    public void updateCodebook(Mat frame) {
        if (!initialized) {
            initCodeBook(frame);
            initialized = true;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < frame.rows(); i++) {
            for (int j = 0; j < frame.cols(); j++) {

                double[] currentPixel = frame.get(i, j);
                Codebook pixelCodeBook = codebooks[i][j];
                boolean existedCodebookMatch = false;
                for (int elemIndex = 0; elemIndex < pixelCodeBook.elementsCount; elemIndex++) {
                    if (matchPixel(currentPixel, pixelCodeBook.elements[elemIndex])) {
                        adjustThresholds(currentPixel, pixelCodeBook.elements[elemIndex]);
                        existedCodebookMatch = true;
                        break;
                    }
                }
                if (!existedCodebookMatch) {
                    createNewBlob(currentPixel, pixelCodeBook);
                }
            }
            System.out.println("Handled time" + (System.currentTimeMillis() - startTime));
        }
    }

    private void initCodeBook(Mat frame) {
        int cols = frame.cols();
        int rows = frame.rows();
        codebooks = new Codebook[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                codebooks[i][j] = new Codebook();
            }
        }
    }

    public void clearStaleEntries(){
        for (int i = 0; i < 640; i++) {
            for (int j = 0; j < 480; j++) {
                Codebook codebook = codebooks[i][j];
                codebooks[i][j] = filterStaleElements(codebook);
            }
        }
    }

    private void createNewBlob(double[] currentPixel, Codebook pixelCodeBook) {
        pixelCodeBook.elementsCount++;
        CodebookElement codebookElement = new CodebookElement();
        for (int channel = 0; channel < 3; channel++) {
            codebookElement.maxVal[channel] = (int) currentPixel[channel];
            codebookElement.minVal[channel] = (int) currentPixel[channel];
        }
        codebookElement.t_last_update = pixelCodeBook.t;
        codebookElement.stale = 0;
        pixelCodeBook.elements[pixelCodeBook.elementsCount] = codebookElement;
    }

    private void adjustThresholds(double[] currentPixel, CodebookElement codebookElement) {
        for (int channel = 0; channel < 3; channel++) {
            if (codebookElement.minVal[channel] >= currentPixel[channel]) {
                codebookElement.minVal[channel] = (int) currentPixel[channel];
            }
            if (codebookElement.maxVal[channel] <= currentPixel[channel]) {
                codebookElement.maxVal[channel] = (int) currentPixel[channel];
            }
        }
    }

    private boolean matchPixel(double[] currentPixel, CodebookElement codebookElement) {
        int matchedChannels = 0;
        for (int channel = 0; channel < 3; channel++) {

            if (codebookElement.minVal[channel] <= currentPixel[channel]
                    && codebookElement.maxVal[channel] >= currentPixel[channel]) {
                matchedChannels++;
            }
        }
        return matchedChannels == 3;
    }



    public Mat backGroundDiff(Mat frame){
        Mat toReturn = new Mat(frame.rows(), frame.cols(), CvType.CV_8UC1);
        for (int i = 0; i < frame.rows(); i++) {
            for (int j = 0; j < frame.cols(); j++) {
                Codebook codebookForPixel = codebooks[i][j];
                double[] currentPixel = frame.get(i, j);
                for (int elemIndex = 0; elemIndex < codebookForPixel.elementsCount; elemIndex++) {
                    if(matchPixel(currentPixel, codebookForPixel.elements[elemIndex])){
                        toReturn.put(i,j, new int[]{255});
                        break;
                    } else {
                        toReturn.put(i,j,new int[]{0});
                    }
                }
            }
         }
        return toReturn;
    }

    private Codebook filterStaleElements(Codebook codebook) {
        Codebook toReturn = new Codebook();
        for(int elemIndex = 0; elemIndex < codebook.elementsCount; elemIndex++){
            CodebookElement elementToCheck = codebook.elements[elemIndex];
            if(elementToCheck.stale < 25){
                toReturn.elements[toReturn.elementsCount] = elementToCheck;
                toReturn.elementsCount++;
            }
        }
        return toReturn;
    }

    private static class Codebook {
        CodebookElement[] elements = new CodebookElement[200];
        int elementsCount = 0;
        int t = 0;

        public Codebook() {
            elements[0] = new CodebookElement();
        }
    }

    private static class CodebookElement {
        int[] minVal = {0,0,0};
        int[] maxVal = {0,0,0};

        int[] minValThreshold = new int[3];
        int[] maxValThreshold = new int[3];

        int t_last_update = 0;
        int stale = 0;
    }
}
