package eyesimo.processor;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.bankmanager.tools.CurrentBankCharsInfo;
import eyesimo.processor.judgers.AnalyzeResult;
import eyesimo.processor.segmentators.ImageFullChars;

public class PafSmartProcessor extends PafProcessor {
	
	private CurrentBankCharsInfo[][] controlChars;
	private CurrentBankCharsInfo[][] learningChars;
	private CommonBankCharsInfo sourceBank;
	private CommonBankCharsInfo learningBank;
	
	
	public PafSmartProcessor() {
		//default k = 24
		super( 24 );
	}
	
	public PafSmartProcessor(int k) {
		super(k);
	}
	
	
	/* 
	 * bankInfo  - chars bank
	 * N - chars bank splits count
	 * c - count of random elements which taking from each split 
	 */
	public int learnParamK( CommonBankCharsInfo bankInfo, int N, int c, int maxNeighbors) {
		
		int kBackup = super.getNeighborParam();
		sourceBank = bankInfo;



		System.out.println( "Start k: " + super.getNeighborParam() );
		
		int optimalK = 0;
		double minCV = Double.MAX_VALUE;
		
		for (int k = 1; k < maxNeighbors; k++) {
			
			double start = System.currentTimeMillis();
			
			super.setParamK(k);
			
			double currentCV = 0;

			for (int u = 0; u < N; u++) {

				initDataToLearn(sourceBank, N, c);

				double empiricalRisk = 0;

				for (int i = 0; i < N; i++) {
					for (int j = 0; j < c; j++) {

						ImageFullChars currentControlChars = controlChars[i][j].getBankChars();

						AnalyzeResult analyzedControlChars = super.doAnalyze(currentControlChars, learningBank);

						empiricalRisk += getFailIndicator(analyzedControlChars, controlChars[i][j]);
					}
				}
				empiricalRisk /= (c * N);
				currentCV += empiricalRisk;
			}
			
			currentCV /= N;
			
			double end = System.currentTimeMillis();
			System.out.println("k = " + k + " CV: " + currentCV + " Time: " + (end - start) / 1000 + " sec");
			
			if ( minCV > currentCV ) {
				minCV    = currentCV;
				optimalK = k;
			}
		}
		
		super.setParamK(kBackup);
		
		System.out.println("Optimal k: " + optimalK + " CV of optimal k: " + minCV);
		
		return optimalK;
	}
	
	private void initDataToLearn( CommonBankCharsInfo bankInfo, int N, int c ) {
		
		int countChars = bankInfo.getBankInfo().length;
		CurrentBankCharsInfo[] allBankChars = bankInfo.getBankInfo();
		
		int countCharsInSplit = countChars / N;
		int modFlag = countChars % N == 1 ? 1 : 0;
		
		//init split data
		CurrentBankCharsInfo[][] charsSplits = new CurrentBankCharsInfo[N][];
		
		charsSplits[0] = new CurrentBankCharsInfo[countCharsInSplit + modFlag];
		for (int i = 1; i < N; i++) {
			charsSplits[i] = new CurrentBankCharsInfo[countCharsInSplit];
		}
		
		int k = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < charsSplits[i].length; j++) {
				charsSplits[i][j] = allBankChars[k];
				k++;
			}
		}
		
		//init control data
		controlChars = new CurrentBankCharsInfo[N][];
		
		for (int i = 0; i < N; i++) {
			controlChars[i] = new CurrentBankCharsInfo[c];
		}
		
		//init data without control data
		
		learningChars = new CurrentBankCharsInfo[N][];
		
		learningChars[0] = new CurrentBankCharsInfo[countCharsInSplit + modFlag - c];
		for (int i = 1; i < N; i++) {
			learningChars[i] = new CurrentBankCharsInfo[countCharsInSplit - c];
		}
		
		//fill learning and control data
		
		fillControlAndLearningData(charsSplits, N, c);
		
		//fill learning bank
		
		int learningBankLength = 0;
		
		for (int i = 0; i < N; i++) {
			learningBankLength += learningChars[i].length;
		}
		
		CurrentBankCharsInfo[] learningBankChars = new CurrentBankCharsInfo[ learningBankLength ];
		
		k = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < learningChars[i].length; j++) {
				
				learningBankChars[k] = learningChars[i][j];
				k++;
			}
		}
		
		learningBank = new CommonBankCharsInfo(bankInfo.getClassCount(), bankInfo.getClassNames(), learningBankChars);
	}
	
	private void fillControlAndLearningData( CurrentBankCharsInfo[][] charsSplits, int N,  int c ) {
		
		for (int i = 0; i < N; i++) {
			
			int splitLength = charsSplits[i].length;
			Vector<Integer> randomInd = new Vector<Integer>(c);
			
			Random generator = new Random();
			generator.setSeed(System.currentTimeMillis());
			
			Set<Integer> indSet = new HashSet<Integer>();
				
			while (indSet.size() != c) {
				indSet.add( generator.nextInt(splitLength) );
			}
				
				
			randomInd.addAll(indSet);
			
			for (int j = 0; j < c; j++) {
				controlChars[i][j] = charsSplits[i][randomInd.get( j )];
			}
			
			int k = 0;
			for (int j = 0; j < splitLength; j++) {
				
				if ( randomInd.contains(j) ) continue;
				
				learningChars[i][k] = charsSplits[i][j];
				k++;
			}
			
		}
		
	}
	
	private int getFailIndicator( AnalyzeResult analyzedObject, CurrentBankCharsInfo bankInfo) {
		
		int out = analyzedObject.getClassIndex() != bankInfo.getClassIndex() ? 1 : 0;
		
		return out;
	}
}