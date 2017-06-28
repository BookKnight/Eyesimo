package tests.io;

import java.awt.image.BufferedImage;
import java.io.File;

import eyesimo.bankmanager.io.ImageReader;
import eyesimo.bankmanager.io.ImageReaderException;
import eyesimo.bankmanager.io.ImageWriter;

public class ImageReaderWriterTest {
	
	public static void main(String args[]) {
		ImageReader imageReader = new ImageReader();
		
		//test read and write images from catalogue
		File imageCatalogue = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Images/");
		
		try {
			imageReader.setImageCatalogue( imageCatalogue );
		} catch (ImageReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedImage readedImage = imageReader.readImage(0);
		
		ImageWriter imageWriter = new ImageWriter();
		imageWriter.writeImage(imageCatalogue, readedImage, (imageReader.getLastReadedImageName() + "_0") );
		
		//test read and write current image
		File currentImage = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Images/testYacht.jpeg");
		
		readedImage = imageReader.readImage(currentImage);
		
		imageWriter.writeImage(imageCatalogue, readedImage, (imageReader.getLastReadedImageName() + "_current") );
		
	}
	
}
