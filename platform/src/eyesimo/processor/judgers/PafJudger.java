package eyesimo.processor.judgers;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.bankmanager.tools.CurrentBankCharsInfo;
import eyesimo.bankmanager.tools.CharsMaker;
import eyesimo.processor.segmentators.ImageColorChars;
import eyesimo.processor.segmentators.ImageFullChars;
import eyesimo.processor.segmentators.PafSegmentator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import static java.lang.Math.abs;

/**
 * Created by Anton_Erde on 12.03.2016.
 */
public class PafJudger {
	
	private int classCount;
	private Vector<ObjectToCompare> bankObjects;
	private String[] classNames;

	public PafJudger() {
		classCount = 0;
		bankObjects = new Vector<ObjectToCompare>();
		classNames = null;
		
	}
	
    public AnalyzeResult doAnalyze (ImageFullChars objectToAnalyse, CommonBankCharsInfo charsBankInfo, int k) {
    	
    	resetData();
    	
    	//prepare data to analysis
    	initObjectsToCompare(objectToAnalyse, charsBankInfo);
    
    	ObjectToCompare comparator = new ObjectToCompare();
		bankObjects.sort(comparator);
		
		//do analysis
		int classInd = Integer.MIN_VALUE;
		double maxSum = 0;
		
		for (int i = 0; i < classCount; i++) {
			double tmpSum = 0;

			for (int j = 0; j < k; j++) {
				
				//get class of j neighbor
				int neighborClass = bankObjects.get(j).getObjClass();
				
				//calculate and sum weight of neighbor;
				if (i == neighborClass) tmpSum += getKernel(j, k);
			}
			
			if (tmpSum >= maxSum) {
				classInd = i;
				maxSum = tmpSum;
				
				//System.out.println("Weight of: " + classNames[classInd] + " is " + maxSum);
			}
			
			if (tmpSum < maxSum) {
				//System.out.println("Weight of: " + classNames[i] + " is " + tmpSum);
			}
		}
		
		return new AnalyzeResult(classNames[classInd], classInd);
    }

    private void initObjectsToCompare( ImageFullChars object, CommonBankCharsInfo charsBankInfo) {

		bankObjects.clear();
    	
    	classCount = charsBankInfo.getClassCount();
    	classNames = charsBankInfo.getClassNames();
    	CurrentBankCharsInfo[] bankChars = charsBankInfo.getBankInfo();
    	
    	for (int j = 0; j < bankChars.length; j++) {
    			
    		double euclideLen = getEuclideLength(object, bankChars[j].getBankChars());

    		ObjectToCompare tmp = new ObjectToCompare();
    		tmp.setObjClass(bankChars[j].getClassIndex());
    		tmp.setLength(euclideLen);
    			
    		bankObjects.addElement(tmp);
    	}
    }
    
    public static double getEuclideLength(ImageFullChars img_1, ImageFullChars img_2) {

        int charsHeight = img_1.getColorInfos().length; //Colors vals

        ImageColorChars[] colorInfos_1 = img_1.getColorInfos();
        ImageColorChars[] colorInfos_2 = img_2.getColorInfos();


		double fieldsDifSum = 0;
		double areasDifSum = 0;
		double peripheryDifSum = 0;

        for (int i = 0; i < charsHeight; i++) {

        	double fieldsDif = ( colorInfos_1[i].getFieldsCount() - colorInfos_2[i].getFieldsCount() );
        	double tmp = Math.abs( fieldsDif );
            fieldsDifSum    += tmp;

			double areasDif = ( colorInfos_1[i].getMiddleArea() - colorInfos_2[i].getMiddleArea() );
            tmp = Math.abs( areasDif );
            areasDifSum += tmp;

			double peripheryDif = ( colorInfos_1[i].getMiddlePeriphery() - colorInfos_2[i].getMiddlePeriphery() );
            tmp = Math.abs( peripheryDif );
            peripheryDifSum += tmp;
        }

        return ( areasDifSum + fieldsDifSum + peripheryDifSum ) / (charsHeight * 3) ;
    }
    
    public double getRatingOfNeighborhood(ImageFullChars objectToAnalyse, CommonBankCharsInfo charsBankInfo, int i, int k) {
    	
    	//prepare data to analysis
    	initObjectsToCompare(objectToAnalyse, charsBankInfo);
    
    	ObjectToCompare comparator = new ObjectToCompare();
		bankObjects.sort(comparator);
		
	
		double rating = 0;
		int len = bankObjects.size() - k;
		for (int j = 0; j < k; j++) {

			int neighborClass = bankObjects.get(j).getObjClass();
				
			if (i == neighborClass) rating += getKernel(j, k);
		}

		
		return rating;
    }
    
    /*quartic kernel
     * 
     * i - index of neighbor
     * k - max index of neighbor, which defines radius of neighborhood
     */
    private double getKernel(int i, int k) {
    	
    	double r = bankObjects.get(i).getLength() / bankObjects.get(k).getLength();
    	
    	return (1 - r*r) * (1 - r*r);
    }
    
    private void resetData() {
    	classCount = 0;
    	bankObjects.clear();
    	classNames = null;
    }

}
