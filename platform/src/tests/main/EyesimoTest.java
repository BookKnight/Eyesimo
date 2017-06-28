package tests.main;

import java.io.File;

import eyesimo.main.Eyesimo;
import eyesimo.main.EyesimoAPI;

public class EyesimoTest {
	public static void main(String args[]) {




		//////////////////////////////////////////////////////////
		//Test classification of current image                  //
		//////////////////////////////////////////////////////////
		
		File testYachtFile = new File("./platform/src/tests/Test Imagebank/control yachts/i_1341.jpeg");

		Eyesimo eyesimo = new Eyesimo();

		//eyesimo.makeCharsForWholeBank(100, 100);


		long start = System.currentTimeMillis();
		eyesimo.classify(testYachtFile);
		long end = System.currentTimeMillis();
		System.out.println("Classification time with start k: " + (double) (end - start) / 1000.0 );

		System.out.println();
		System.out.println("/////////////////////////////");
		System.out.println("Current K: " + eyesimo.getCurrentK());


		int learnedK = eyesimo.learnK();
		eyesimo.setK( learnedK );
		
		System.out.println("Current K: " + eyesimo.getCurrentK());
		System.out.println("/////////////////////////////");
		System.out.println();

		start = System.currentTimeMillis();
		eyesimo.classify(testYachtFile);
		end = System.currentTimeMillis();
		System.out.println("Classification time with optimal k: " + (double) (end - start) / 1000.0 );
		
		
		//////////////////////////////////////////////////////////
		//Test classification of directory which contains images//
		//////////////////////////////////////////////////////////
		
		File testYachtsDir = new File("./platform/src/tests/Test Imagebank/control yachts/");
		File testOtherDir = new File("./platform/src/tests/Test Imagebank/control other/");

		start = System.currentTimeMillis();
		eyesimo.classify(testYachtsDir);
		end = System.currentTimeMillis();
		System.out.println("Classification time of " + testYachtsDir.listFiles().length + " yachts images: " + (double) (end - start) / 1000.0 );

		eyesimo.classify(testOtherDir);
				
		System.out.println();
		System.out.println("/////////////////////////////");
		System.out.println("Current K: " + eyesimo.getCurrentK());
				
		learnedK = eyesimo.learnK();
		eyesimo.setK( learnedK );
				
		System.out.println("Current K: " + eyesimo.getCurrentK());
		System.out.println("/////////////////////////////");
		System.out.println();
				
		eyesimo.classify(testYachtsDir);
		eyesimo.classify(testOtherDir);
		
		/*
		//////////////////////////////////////////////////////////
		//Test chars making without settings changing           //
		//////////////////////////////////////////////////////////
		
		File objectsCatalog = new File("./platform/src/tests/Test Imagebank/control yachts/");
		File charsCatalog   = new File("./platform/src/tests/Test Charsbank/control yachts/");
		
		int defWidth = eyesimo.getCurrentStandardWidth();
		int defHeight = eyesimo.getCurrentStandardHeight();
		
		eyesimo.makeChars(objectsCatalog, charsCatalog, defWidth + 1, defHeight + 1);
		
		int curWidth = eyesimo.getCurrentStandardWidth();
		int curHeight = eyesimo.getCurrentStandardHeight();
		
		if (defWidth == curWidth && defHeight == curHeight) System.out.println("Sizes have not modified.");
		System.out.println();
		

		//////////////////////////////////////////////////////////
		//Test chars making for whole bank                      //
		//////////////////////////////////////////////////////////
		
		File testImageBank = new File("./platform/src/tests/Test Imagebank/");
		File testCharsBank = new File("./platform/src/tests/Test Charsbank/");
		
		eyesimo.setImageBankDir(testImageBank);
		eyesimo.setCharsBankDir(testCharsBank);
		
		eyesimo.makeCharsForWholeBank(50, 50);
		*/
	}
	
}
