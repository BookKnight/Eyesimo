package tests.io;

import java.io.File;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.bankmanager.io.CharsWriter;
import eyesimo.processor.segmentators.*;

public class CharsReaderWriterTest {
	
	public static void main(String args[]) {
		
		CharsReader charsReader = new CharsReader();
		CharsWriter charsWriter = new CharsWriter();
		
		String charsCataloguePath = "/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Chars/";
		String currentCharPath = "/home/antonerde/MyProgs/workspace/Eyesimo/platform/src/tests/Test Chars/testYacht.txt";
		
		File currentCharFile = new File( currentCharPath );
		
		ImageFullChars currentFullInfo = charsReader.readFullInfo(currentCharFile);
		
		File charsDir = new File( charsCataloguePath );
		String newCharFileName = currentCharFile.getName().substring(0, currentCharFile.getName().length()- 4) + "_new.txt";
		charsWriter.write( charsDir, currentFullInfo,  newCharFileName);
	}
	
}
