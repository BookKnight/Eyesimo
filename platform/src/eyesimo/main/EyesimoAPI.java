package eyesimo.main;

import eyesimo.processor.judgers.AnalyzeResult;

import java.awt.geom.Point2D;
import java.io.File;

public interface EyesimoAPI {

	void makeChars(File objectsCatalog, File charsCatalog, int specifiedWidth, int specifiedHeight);

	//make chars for whole source bank with specified standard size of images (eyesimo settings will be changed)
	void makeCharsForWholeBank(int standardWidth, int standardHeight);

	
	AnalyzeResult classify(File imageFile);

	
	int learnK();

	void setK(int _k);

	
	void setImageBankDir(File newBankDir);

	void setCharsBankDir(File newBankDir);

	
	int getCurrentK();

	int getCurrentStandardWidth();

	int getCurrentStandardHeight();

	void saveSettings();

	void updateSettings();

	Point2D.Double[] getChartData(String choseClass);

	void extractSamples( String choseClass, double marginLimit);

	String[] getClassNames();

}