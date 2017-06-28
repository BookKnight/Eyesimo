package eyesimo.processor;

import java.awt.image.BufferedImage;

import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.processor.filters.ClearColorFilter;
import eyesimo.processor.judgers.AnalyzeResult;
import eyesimo.processor.judgers.PafJudger;
import eyesimo.processor.segmentators.ImageFullChars;
import eyesimo.processor.segmentators.PafSegmentator;

public class PafProcessor {

	private ClearColorFilter clearColorFilter;
	private PafSegmentator pafSegmentator;
	private PafJudger pafJudger;
	
	//parameters of processor
	private int kNeighbors;
	
	//work data
	transient BufferedImage lastAnalyzedImage;
	transient ImageFullChars lastChars;
	transient String lastImageName;
	
	
	public PafProcessor(int _kNeighbors) {
		
		clearColorFilter = new ClearColorFilter();
		pafSegmentator   = new PafSegmentator();
		pafJudger        = new PafJudger();
		
		kNeighbors = _kNeighbors;
	}
	
	public AnalyzeResult doAnalyze(BufferedImage imageToAnalyze, String imageName, CommonBankCharsInfo bankInfo) {
		
		BufferedImage filteredImage = clearColorFilter.filterImage(imageToAnalyze);
		

		
		ImageFullChars imageChars   = pafSegmentator.getFullInfo(filteredImage, imageName);
		
		lastAnalyzedImage = imageToAnalyze;
		lastChars = imageChars;
		lastImageName = imageName;
		
		AnalyzeResult out = doAnalyze(imageChars, bankInfo);
		
		return out;
	}
	
public AnalyzeResult doAnalyze(ImageFullChars objectToAnalyze, CommonBankCharsInfo bankInfo) {
		
		
		AnalyzeResult out = pafJudger.doAnalyze(objectToAnalyze, bankInfo, kNeighbors);
		
		return out;
	}
	
    public void setParamK(int _k) {
    	kNeighbors = _k;
    }
	
	public int getNeighborParam() {
		return kNeighbors;
	}
	
	//get work data methods
	public BufferedImage getLastAnalyzedImage() {
		return lastAnalyzedImage;
	}
	
	public ImageFullChars getLastChars() {
		return lastChars;
	}
	
	public String getLastImageName() {
		return lastImageName;
	}
}
