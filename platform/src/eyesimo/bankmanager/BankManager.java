package eyesimo.bankmanager;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.bankmanager.io.CharsWriter;
import eyesimo.bankmanager.io.ImageReader;
import eyesimo.bankmanager.io.ImageWriter;
import eyesimo.bankmanager.tools.CharsExtractor;
import eyesimo.bankmanager.tools.CharsMaker;
import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.bankmanager.tools.SamplesExtractor;
import eyesimo.processor.segmentators.ImageFullChars;
import eyesimo.settingsmanager.EyesimoSettings;

public class BankManager {
	
	//IO classes
	ImageReader imageReader;
	ImageWriter imageWriter;
	CharsReader charsReader;
	CharsWriter charsWriter;
	
	//Tools classes
	CharsExtractor charsExtractor;
	CharsMaker charsMaker;
	SamplesExtractor samplesExtractor;
	
	public BankManager() {
		
		imageReader = new ImageReader();
		imageWriter = new ImageWriter();
		charsReader = new CharsReader();
		charsWriter = new CharsWriter();
		
		charsExtractor = new CharsExtractor();
		charsMaker = new CharsMaker();
		samplesExtractor = new SamplesExtractor();
	}
	
	public BufferedImage readImage(File imageFile) {
		return imageReader.readImage(imageFile);
	}
	
	public String getLastReadedImageName() {
		return imageReader.getLastReadedImageName();
	}
	
	public void writeImage(File imageCatalogue, BufferedImage image, String imageName) {
		imageWriter.writeImage(imageCatalogue, image, imageName);
	}
	
	public ImageFullChars readFullInfo(File charsFile) {
		return charsReader.readFullInfo(charsFile);
		
	}
	
	public void writeChars(File bankCataloguePath, ImageFullChars fullInfo, String fileName) {
		charsWriter.write(bankCataloguePath, fullInfo, fileName);
	}
	
	public CommonBankCharsInfo getBankChars(File charsBankDir) {
		return charsExtractor.getBankChars(charsBankDir);
		
	}
	
	public void makeChars(File objectsCatalog, File charsCatalog, int standardWidth, int standardHeight) {
		
		int backupWidth = charsMaker.getWidth();
		int backupHeight = charsMaker.getHeight();
		
		charsMaker.setSizes(standardWidth, standardHeight);
		charsMaker.makeChars(objectsCatalog, charsCatalog);
		charsMaker.setSizes(backupWidth, backupHeight);
		
	}
	
	public void makeCharsForWholeBank(File imageBankDir, File charsBankCatalog, int standardWidth, int standardHeight) {
		
		File[] imagesCatalogs = imageBankDir.listFiles();
		File[] charsCatalogs  = new File[imagesCatalogs.length];
		
		for (int i = 0; i < charsCatalogs.length; i++) {
			String path = charsBankCatalog.getAbsolutePath() + "/" + imagesCatalogs[i].getName();
			charsCatalogs[i] = new File(path);
			
			if ( !charsCatalogs[i].exists() ) charsCatalogs[i].mkdirs();
		}
		
		for (int i = 0; i < charsCatalogs.length; i++) {
			makeChars(imagesCatalogs[i], charsCatalogs[i], standardWidth, standardHeight);
		}
		
	}

	public Point2D.Double[] getChartData( EyesimoSettings settings, String drawingClass) {

		samplesExtractor.setSettings( settings );
		samplesExtractor.setChoseClass( drawingClass );

		return samplesExtractor.getChartData();
	}

	public void extractSamples(EyesimoSettings settings, String choseClass, double marginLimit) {
		samplesExtractor.setSettings( settings );

		samplesExtractor.setChoseClass(choseClass);
		samplesExtractor.setMarginLimit(marginLimit);

		samplesExtractor.extractSamples();
	}

	
}
