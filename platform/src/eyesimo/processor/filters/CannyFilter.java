package eyesimo.processor.filters;

import java.awt.image.BufferedImage;

public class CannyFilter {
	

	public BufferedImage canny(BufferedImage image, double sigma, double threshHi, double threshLo) {
		
		GrayScaleFilter grayFilter = new GrayScaleFilter();
		
		MatrixF graySrcImg = grayFilter.filter(image);
		
		MatrixF tmp = CannyPreproc.canny(graySrcImg, (float) sigma, (float) threshHi, (float) threshLo);
		
		BufferedImage result = tmp.convertCannyMatrixF2BufferedImage(tmp);
		
		return result;
	}
	
}
