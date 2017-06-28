package tests.processors;

import java.awt.image.BufferedImage;
import java.io.File;

import eyesimo.bankmanager.io.ImageReader;
import eyesimo.bankmanager.tools.CharsExtractor;
import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.processor.PafProcessor;
import eyesimo.processor.PafSmartProcessor;
import eyesimo.processor.judgers.AnalyzeResult;

public class PafSmartProcessorTest {
	public static void main(String args[]) {
		
		
		PafSmartProcessor pafSmartProcessor = new PafSmartProcessor( 20 );
		CharsExtractor charsExtractor = new CharsExtractor();

		File bankDir = new File("/home/antonerde/MyProgs/workspace/Eyesimo/characteristics-bank/");
		CommonBankCharsInfo bankCharsInfo = charsExtractor.getBankChars(bankDir);
		
		
		pafSmartProcessor.learnParamK(bankCharsInfo, 30, 10, 50);
	
	}
}
