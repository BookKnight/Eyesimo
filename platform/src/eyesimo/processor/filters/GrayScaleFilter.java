package eyesimo.processor.filters;

import java.awt.image.*;

class GrayScaleFilter {
	
	public MatrixF filter( BufferedImage srcImg ) {
		
		//get image source data
		int height = srcImg.getHeight();
		int width  = srcImg.getWidth();
		int[] tmpData = srcImg.getRGB(0, 0, width, height, null, 0, width);
		
		//convert rgb to grayscale and fill new data array
		float[] data  = new float[width * height];
		for (int i = 0; i < width * height; i++) {
			
			int r = (tmpData[i] >> 16) & 0xFF;
	        int g = (tmpData[i] >> 8) & 0xFF;
	        int b = tmpData[i] & 0xFF;
	        
	        data[i] = (float) ( r * 0.2989 + g * 0.5870 + b * 0.1140 );
		}
		
		//return filtered image as MatrixF
		return new MatrixF(height, width, data);
	}
	
}
