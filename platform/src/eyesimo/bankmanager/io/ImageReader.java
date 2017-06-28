package eyesimo.bankmanager.io;

import eyesimo.bankmanager.tools.ImageManipulator;
import eyesimo.processor.filters.ClearColorFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ImageReader {
    private int imageCount;
    private File imageCatalogue;
    private String lastReadedImageName;
    

    public ImageReader() {
        imageCatalogue = null;
        lastReadedImageName = null;
    }
    
    public void setImageCatalogue(File _imageCatalogue) throws ImageReaderException {
    	
    	if ( _imageCatalogue.isDirectory() ) imageCatalogue = _imageCatalogue;
    	else throw new ImageReaderException("fail to set image catalogue to read");
    	
    	initImageCount();
    	
    }
    
  //!!!!To do: add exceptions!!!
    public BufferedImage readImage(File imageFile) {
    	
        BufferedImage readedImage = null;
        try {
            readedImage = ImageIO.read( imageFile );
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String imageName = imageFile.getName();
        
        lastReadedImageName = imageName.substring(0, imageName.length() - 5);
        
        return readedImage;
    	
    }

    public BufferedImage readImage(int index) throws IndexOutOfBoundsException {

        File imageDir = imageCatalogue;
        String[] imageNames = imageDir.list();
        imageCount = imageNames.length;

        if (index >= imageCount) throw new IndexOutOfBoundsException();

        File currentImage = new File(imageCatalogue + "/" + imageNames[index]);

        BufferedImage readedImage = null;
        try {
            readedImage = ImageIO.read(currentImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lastReadedImageName = imageNames[index].substring(0, imageNames[index].length() - 5);

        return readedImage;
    }

    private void initImageCount() {
        File imageDir = imageCatalogue;

        String[] imageNames = imageDir.list();

        Arrays.sort(imageNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        imageCount = imageNames.length;
    }

    public int getImageCount() {
        initImageCount();

        return imageCount;
    }

    public String getLastReadedImageName() {
        return lastReadedImageName;
    }
}
