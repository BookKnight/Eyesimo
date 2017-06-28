package tests.tools;

import eyesimo.bankmanager.tools.*;

import java.io.File;

public class CharsMakerTest {
	public static void main( String args[] ) {
		
		File testObjsCatalogue = new File("./platform/src/tests/Test Images/");
		File testCharsCatalogue = new File("./platform/src/tests/Test Chars/");
		
		CharsMaker charsMaker = new CharsMaker();
		charsMaker.setSizes(389, 245);
		charsMaker.makeChars(testObjsCatalogue, testCharsCatalogue);
		
		//To fix: incorrect name output (ImageReader)
	}
}
