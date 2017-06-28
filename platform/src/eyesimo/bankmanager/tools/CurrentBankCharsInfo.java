package eyesimo.bankmanager.tools;

import eyesimo.processor.segmentators.ImageFullChars;

public class CurrentBankCharsInfo {
	int classIndex;
	ImageFullChars bankChars;
	
	
	
	public CurrentBankCharsInfo( int ind, ImageFullChars _bankChars) {
		classIndex = ind;
		bankChars= _bankChars;
	}
	
	public int getClassIndex() {
		return classIndex;
	}
	
	public ImageFullChars getBankChars() {
		return bankChars;
	}
}
