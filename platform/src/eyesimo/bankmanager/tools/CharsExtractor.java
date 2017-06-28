package eyesimo.bankmanager.tools;

import java.io.File;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.processor.segmentators.ImageFullChars;

public class CharsExtractor {

	public CommonBankCharsInfo getBankChars(File charsBankDir) {
		
		//get list of classes
    	File[] objClasses = charsBankDir.listFiles();
    	int classCount = objClasses.length;
    	
    	//initClassNames
    	String[] classNames = new String[classCount];
    	for (int i = 0; i < classCount; i++) {
    		classNames[i] = objClasses[i].getName();
    	}
    	
    	//get sizes of classes
    	int bankObjsCount = 0;
    	File[][] bankCharsFiles = new File[classCount][];
    	for (int i = 0; i < classCount; i++) {
    		int objsCount = objClasses[i].listFiles().length;
    		
    		bankCharsFiles[i] = new File[objsCount];
    		bankCharsFiles[i] = objClasses[i].listFiles();
    		bankObjsCount += objsCount;
    	}
    	
    	//fill vector of bank characteristics
    	CharsReader charsReader = new CharsReader();
    	CurrentBankCharsInfo[] bankChars = new CurrentBankCharsInfo[bankObjsCount];
    	
    	int k = 0;
    	for (int i = 0; i < classCount; i++) {
    		for (int j = 0; j < bankCharsFiles[i].length; j++) {
    			
    			ImageFullChars readedChars = charsReader.readFullInfo(bankCharsFiles[i][j]);
    			
    			bankChars[k] = new CurrentBankCharsInfo(i, readedChars);
    			
    			k++;
    		}
    	}
    	
    	CommonBankCharsInfo out = new CommonBankCharsInfo(classCount, classNames, bankChars);
    	
    	return out;
   }

	public CommonBankCharsInfo getCurrentClassChars(File charsBankDir, String className ) {

		//get list of classes
		File[] objClasses = charsBankDir.listFiles();

		int classInd = -1;

		for ( int i = 0; i < objClasses.length; i++ ) {

			if ( objClasses[i].getName().equals( className ) ) {
				classInd = i;
				break;
			}
		}

		//get sizes of classes
		int bankObjsCount = 0;
		File[][] bankCharsFiles = new File[1][];
		int objsCount = objClasses[classInd].listFiles().length;

		bankCharsFiles[0] = new File[objsCount];
		bankCharsFiles[0] = objClasses[classInd].listFiles();
		bankObjsCount += objsCount;

		//fill vector of bank characteristics
		CharsReader charsReader = new CharsReader();
		CurrentBankCharsInfo[] bankChars = new CurrentBankCharsInfo[bankObjsCount];

		int k = 0;

		for (int j = 0; j < bankCharsFiles[classInd].length; j++) {

			ImageFullChars readedChars = charsReader.readFullInfo(bankCharsFiles[0][j]);

			bankChars[k] = new CurrentBankCharsInfo( classInd, readedChars);

			k++;
		}


		CommonBankCharsInfo out = new CommonBankCharsInfo(1, new String[]{className}, bankChars);

		return out;
	}

	public CommonBankCharsInfo getCharsWithoutChoseClass( File charsBankDir, String choseClass ) {

		//get list of classes
		File[] objClasses = charsBankDir.listFiles();
		int classCount = objClasses.length - 1;

		int classInd = -1;
		for ( int i = 0; i < objClasses.length; i++ ) {
			if ( objClasses[i].getName().equals( choseClass ) ) {
				classInd = i;
				break;
			}
		}

		//initClassNames
		String[] classNames = new String[classCount];
		for (int i = 0; i < classCount; i++) {
			classNames[i] = objClasses[i].getName();
		}

		//get sizes of classes
		int bankObjsCount = 0;
		File[][] bankCharsFiles = new File[classCount][];
		for (int i = 0; i < classCount; i++) {
			int objsCount = objClasses[i].listFiles().length;

			bankCharsFiles[i] = new File[objsCount];
			bankCharsFiles[i] = objClasses[i].listFiles();
			bankObjsCount += objsCount;
		}

		//fill vector of bank characteristics
		CharsReader charsReader = new CharsReader();
		CurrentBankCharsInfo[] bankChars = new CurrentBankCharsInfo[bankObjsCount];

		int k = 0;
		for (int i = 0; i < classCount; i++) {
			for (int j = 0; j < bankCharsFiles[i].length; j++) {

				ImageFullChars readedChars = charsReader.readFullInfo(bankCharsFiles[i][j]);

				bankChars[k] = new CurrentBankCharsInfo(i, readedChars);

				k++;
			}
		}

		CommonBankCharsInfo out = new CommonBankCharsInfo(classCount, classNames, bankChars);

		return out;
	}
}
