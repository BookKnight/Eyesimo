package eyesimo.bankmanager.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import eyesimo.bankmanager.io.CharsWriter;
import eyesimo.bankmanager.io.ImageReader;
import eyesimo.bankmanager.io.ImageReaderException;
import eyesimo.processor.filters.ClearColorFilter;
import eyesimo.processor.judgers.PafJudger;
import eyesimo.processor.segmentators.ImageFullChars;
import eyesimo.processor.segmentators.PafSegmentator;

/**
 * Created by Anton_Erde on 15.03.2016.
 */
public class CharsMaker {
	
	int standardWidth;
	int standardHeight;
	
	public CharsMaker() {
		standardWidth = 300;
		standardHeight = 300;
	}
	
	public void setSizes(int _standardWidth, int _standardHeight) {
		standardWidth = _standardWidth;
		standardHeight = _standardHeight;
	}
	
	public int getWidth() {
		return standardWidth;
	}
	
	public int getHeight() {
		return standardHeight;
	}

    public void makeChars(File objectsCatalog, File charsCatalog) {

        ImageReader imageReader = new ImageReader();
        try {
			imageReader.setImageCatalogue(objectsCatalog);
		} catch (ImageReaderException e1) {
			// TODO Auto-generated catch block
			e1.getMessage();
		}
        
        int procCount  = Runtime.getRuntime().availableProcessors();
        int imageCount = imageReader.getImageCount();
        int[] imgPerProc = new int[procCount];

        if ( imageCount > procCount ) {
            for (int i = 0; i < procCount; i++) {
                imgPerProc[i] = imageCount / procCount;
            }
            imgPerProc[0] += (imageCount % procCount) == 0 ? 0 : 1;

        } else  {
            for (int i = 0; i < imageCount; i++) {
                imgPerProc[i] = 1;
            }
        }

        LinkedList<Thread> charsThreads = new LinkedList<>();

        long start = System.currentTimeMillis();

        for (int i = 0; i < procCount; i++) {
            int startInd = 0;
            int endInd = 0;

            for (int j = 0; j < i; j++) {
                startInd += imgPerProc[j];
            }

            for (int j = 0; j <= i; j++) {
                endInd += imgPerProc[j];
            };

            CharsMakerThread charsMakerThread = new CharsMakerThread(imageReader, objectsCatalog, charsCatalog, startInd, endInd, standardWidth, standardHeight);
            charsThreads.add(charsMakerThread.thisThread);
        }

        try {
            for (int i = 0; i < charsThreads.size(); i++) {
                charsThreads.get(i).join();///////////Проверить корректность. Возможно оператор убирает параллельность.
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //long end = System.currentTimeMillis();
        //System.out.println( (double) (end - start) / 1000 + " sec");
    }

    //scale source image to standard size and filter him
    public static BufferedImage prepareObject(BufferedImage srcImage, int standardWidth, int standardHeight) {
        ClearColorFilter imageFilter = new ClearColorFilter();

        BufferedImage tmp = srcImage;
        tmp = ImageManipulator.resize(tmp, standardWidth, standardHeight);
        tmp = imageFilter.filterImage(tmp);

        return tmp;
    }

    //scale source image to standard size, filter him and get subimage at (x0, y0, w, h)
    public static BufferedImage prepareObject(BufferedImage srcImage, int standardWidth, int standardHeight, int x0, int y0, int w, int h) {
    	ClearColorFilter imageFilter = new ClearColorFilter();

        BufferedImage tmp = srcImage;
        tmp = ImageManipulator.resize(tmp, standardWidth, standardHeight);
        tmp = imageFilter.filterImage(tmp);
        tmp = tmp.getSubimage(x0, y0, w, h);

        return tmp;
    }

    private class CharsMakerThread implements Runnable {

        Thread thisThread = null;
        ImageReader imageReader = null;
        File objectClassCatalog = null;
        File charsCatalog = null;
        int standardWidth = 0;
        int standardHeight = 0;
        int startInd = 0;
        int endInd = 0;

         CharsMakerThread(ImageReader _imageReader, File _objectsCatalogue, File charsCatalogue, int _startInd, int _endInd, int _standardWidth, int _standardHeight) {
            imageReader = _imageReader;
            objectClassCatalog = _objectsCatalogue;
            charsCatalog = charsCatalogue;
            standardWidth = _standardWidth;
            standardHeight = _standardHeight;
            startInd = _startInd;
            endInd = _endInd;

             thisThread = new Thread(this);
             thisThread.start();
        }

        @Override
        public void run() {

            for (int i = startInd; i < endInd; i++) {

                BufferedImage bufImg = null;
                String readedImgName = null;

                synchronized ( imageReader ) {
                    bufImg = imageReader.readImage(i);
                    readedImgName = imageReader.getLastReadedImageName();
               }

                PafJudger objectFinder = new PafJudger();

                bufImg = prepareObject(bufImg, standardWidth, standardHeight);

                PafSegmentator infoCollector = new PafSegmentator();
                ImageFullChars fullInfo = infoCollector.getFullInfo(bufImg, readedImgName);

                // test CharsWriter
                CharsWriter charsWriter = new CharsWriter();
                charsWriter.write(charsCatalog, fullInfo, readedImgName);

                System.out.println( "Chars of " + charsCatalog.getName() + "\\" + readedImgName + " writed");
            }

        }
    }
}
