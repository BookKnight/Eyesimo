package eyesimo.processor.segmentators;

import eyesimo.util.AnalystColors;

import java.awt.image.BufferedImage;

/**
 * Created by Anton_Erde on 10.12.2015.
 *
 * This class collect full info about image: colored fields count, their middle periphery and area.
 * This is main class in package - the other class play secondary role.
 *
 * NOTE: THIS CLASS COLLECT INFO ABOUT FILTERED IMAGES!
 */
public class PafSegmentator implements AnalystColors {
    ImageFullChars fullInfo;
    ImageColorChars[] colorInfos;

    public PafSegmentator() {
        fullInfo = null;
        colorInfos = new ImageColorChars[8];
    }


    public ImageFullChars getFullInfo(BufferedImage analysingImage, String imageName) {



        int width = analysingImage.getWidth();
        int height = analysingImage.getHeight();
        int[] imageData = analysingImage.getRGB(0, 0, width, height, (int[])null, 0, width);

        long start = System.currentTimeMillis();
        
        for(int i = 0; i < this.colorInfos.length; ++i) {

            //System.out.println("Collect " + colorPref[i] + " color info... ");
            this.colorInfos[i] = getColorInfo(i, imageData, width, height);
        }
        
        double finish = (double) (System.currentTimeMillis() - start) / 1000;
        //System.out.println("Image name: " + imageName + " Time spent to get full chars: " + finish + " sec");

        this.fullInfo = new ImageFullChars(imageName, width, height, this.colorInfos);
        return this.fullInfo;
    }
    
    public ImageColorChars getColorInfo(int colorInd, int[] imageData, int w, int h) {
        EdgesFinder edgesFinder = new EdgesFinder(colorInd, imageData, w, h);
        edgesFinder.findEdgesChars();
        int fieldsCount = edgesFinder.getFieldsCount();
        int middleArea = edgesFinder.getMiddleArea();
        int middlePeriphery = edgesFinder.getMiddlePeriphery();
        ImageColorChars imageColorInfo = new ImageColorChars(colorInd, fieldsCount, middleArea, middlePeriphery);
        return imageColorInfo;
    }

}
