package tests.judgers;

import java.io.File;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.bankmanager.tools.CharsExtractor;
import eyesimo.processor.judgers.AnalyzeResult;
import eyesimo.processor.judgers.PafJudger;
import eyesimo.processor.segmentators.ImageFullChars;

public class PafJudgerTest {
	public static void main(String args[]) {
		
		
		CharsExtractor charsExtractor = new CharsExtractor();
		CharsReader charsReader = new CharsReader();
		PafJudger pafJudger = new PafJudger();
		
		File testCharsDir = new File("/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Chars/cyberpunk");
		File bankDir = new File("/home/antonerde/MyProgs/workspace/Eyesimo/characteristics-bank/");
		File testCharsFiles[] = testCharsDir.listFiles();
		
		CommonBankCharsInfo bankCharsInfo = charsExtractor.getBankChars(bankDir);
		
		for (int i = 0; i < testCharsFiles.length; i++) {
			File testCharsFile = testCharsFiles[i];
			ImageFullChars testChars = charsReader.readFullInfo(testCharsFile);
	
		
			AnalyzeResult result = pafJudger.doAnalyze(testChars, bankCharsInfo, 60);
			
			//System.out.println( pafJudger.getRatingOfNeighborhood(testChars, bankCharsInfo, 1, 60) );
			
			System.out.println( "Class index: " + result.getClassIndex());
			System.out.println( "Class name: " + result.getClassName() + "\n");
			
		}
	}
}
