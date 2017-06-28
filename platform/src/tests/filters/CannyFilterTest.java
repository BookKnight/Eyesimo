package tests.filters;

import java.awt.image.*;
import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;

import eyesimo.processor.filters.CannyFilter;

public class CannyFilterTest {
	
	public static void main(String args[]) {
		
		File srcImgFile = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Images/testYacht.jpeg");
		
		try {
			BufferedImage srcImg = ImageIO.read(srcImgFile);
			
			CannyFilter cannyFilter = new CannyFilter();
			
			BufferedImage filteredImage = cannyFilter.canny(srcImg, 1.0, 60.0, 20.0);
			
			File imgFile = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Images/CannyFilteredtestYacht.png");
			imgFile.createNewFile();
			
			ImageIO.write(filteredImage, "png", imgFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
