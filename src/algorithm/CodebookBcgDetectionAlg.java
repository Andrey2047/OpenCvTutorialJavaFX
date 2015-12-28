package algorithm;

import org.opencv.core.Mat;

import java.util.Date;

/**
 * Created by Samsung on 12/28/2015.
 */
public class CodebookBcgDetectionAlg {

    private Codebook[][] codebook = new Codebook[640][480];

    public void updateCodebook(Mat frame) {

        for (int i = 0; i < frame.rows(); i++) {
            for (int j = 0; j < frame.cols(); j++) {
                double[] currentPixel = frame.get(i, j);
                Codebook pixelCodeBook = codebook[i][j];

                for (int elemIndex = 0; elemIndex < pixelCodeBook.elementsCount; elemIndex++) {
                    int matchedChannels = 0;
                    for (int channel = 0; channel < 3; channel++) {

                        if (pixelCodeBook.elements[elemIndex].minVal[channel] <= currentPixel[channel]
                                && pixelCodeBook.elements[elemIndex].maxVal[channel] >= currentPixel[channel]) {
                            matchedChannels++;
                        }
                    }
                    if (matchedChannels == 3) {
                        for (int channel = 0; channel < 3; channel++) {
                            if (pixelCodeBook.elements[elemIndex].minVal[channel] >= currentPixel[channel]) {
                                pixelCodeBook.elements[elemIndex].minVal[channel] = (int) currentPixel[channel];
                            }

                            if (pixelCodeBook.elements[elemIndex].maxVal[channel] <= currentPixel[channel]) {
                                pixelCodeBook.elements[elemIndex].maxVal[channel] = (int) currentPixel[channel];
                            }
                        }
                    } else {
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
                }
            }
        }
    }

    private int clearStaleEntries(Codebook[][] codebooks){
        for (int i = 0; i < 640; i++) {
            for (int j = 0; j < 480; j++) {
                Codebook codebook = codebooks[i][j];
                i = filterStaleElements(codebook);
            }
        }
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
    }

    private static class CodebookElement {
        int[] minVal = new int[3];
        int[] maxVal = new int[3];

        int[] minValThreshold = new int[3];
        int[] maxValThreshold = new int[3];

        int t_last_update = 0;
        int stale = 0;
    }
}
