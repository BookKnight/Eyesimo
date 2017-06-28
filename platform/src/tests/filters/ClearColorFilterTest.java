package tests.filters;

import java.awt.image.*;
import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;

import eyesimo.processor.filters.CannyFilter;
import eyesimo.processor.filters.ClearColorFilter;

public class ClearColorFilterTest {
	
	public static void main(String args[]) {
		
		File srcImgFile = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Images/testYacht.jpeg");
		
		try {
			BufferedImage srcImg = ImageIO.read(srcImgFile);
			
			ClearColorFilter filter = new ClearColorFilter();
			
			BufferedImage filteredImage = filter.filterImage(srcImg);
			
			
			File imgFile = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Images/clearColortestYacht.png");
			imgFile.createNewFile();
			
			ImageIO.write(filteredImage, "png", imgFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}