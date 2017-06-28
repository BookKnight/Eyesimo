package tests.processors;

import java.awt.image.BufferedImage;
import java.io.File;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.bankmanager.io.ImageReader;
import eyesimo.bankmanager.tools.CharsExtractor;
import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.processor.PafProcessor;
import eyesimo.processor.judgers.AnalyzeResult;
import eyesimo.processor.judgers.PafJudger;
import eyesimo.processor.segmentators.ImageFullChars;

public class PafProcessorTest {
	public static void main(String args[]) {
		
		
		PafProcessor pafProcessor = new PafProcessor( 60 );
		CharsExtractor charsExtractor = new CharsExtractor();
		ImageReader imageReader = new ImageReader();
		
		File testImagesDir = new File("./platform/src/tests/Test Images/");
		File bankDir = new File("./characteristics-bank/");
		File[] testImagesFiles  = testImagesDir.listFiles(); 
		
		CommonBankCharsInfo bankCharsInfo = charsExtractor.getBankChars(bankDir);
		
		for (int i = 0; i < testImagesFiles.length; i++) {
			BufferedImage testImage = imageReader.readImage( testImagesFiles[i] );
		
			AnalyzeResult result = pafProcessor.doAnalyze(testImage, imageReader.getLastReadedImageName(), bankCharsInfo);
			
			//System.out.println( pafJudger.getRatingOfNeighborhood(testChars, bankCharsInfo, 1, 60) );
			
			System.out.println(imageReader.getLastReadedImageName());
			System.out.println( "Class index: " + result.getClassIndex());
			System.out.println( "Class name: " + result.getClassName() + "\n");
			
		}
	}
}
