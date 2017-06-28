package eyesimo.main;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import eyesimo.bankmanager.BankManager;
import eyesimo.bankmanager.tools.CharsMaker;
import eyesimo.bankmanager.tools.CommonBankCharsInfo;
import eyesimo.processor.PafSmartProcessor;
import eyesimo.processor.judgers.AnalyzeResult;
import eyesimo.settingsmanager.EyesimoSettings;
import eyesimo.settingsmanager.SettingsManager;

public class Eyesimo implements EyesimoAPI {

	///////////////////////////////////////
	//Components

	private BankManager bankManager;
	private PafSmartProcessor smartProc;
	private SettingsManager settingsManager;


	////////////////////////////////////////
	//settings

	EyesimoSettings settings;

	////////////////////////////////////////
	//work data

	private transient CommonBankCharsInfo bankInfo;

	////////////////////////////////////////

	public Eyesimo() {

		bankManager = new BankManager();
		smartProc   = new PafSmartProcessor();
		settingsManager = new SettingsManager();

		settings = new EyesimoSettings();
		updateSettings();

		smartProc.setParamK( settings.getK() );

		updateBankInfo();
	}

	//get full bank chars information
	private void updateBankInfo() {
		bankInfo = bankManager.getBankChars( settings.getCharsBankDir() );
	}

	private void setStandardSizes(int _standardWidth, int _standardHeight) {
		settings.setStWidth( _standardWidth );
		settings.setStHeight( _standardHeight );
	}

	//make chars for specified directory with specified sizes without changing eyesimo settings

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#makeChars(java.io.File, java.io.File, int, int)
     */
	@Override
	public void makeChars(File objectsCatalog, File charsCatalog, int specifiedWidth, int specifiedHeight) {
		bankManager.makeChars(objectsCatalog, charsCatalog, specifiedWidth, specifiedHeight);

	}

	//make chars for whole source bank with specified standard size of images (eyesimo settings will be changed)
		/* (non-Javadoc)
		 * @see eyesimo.main.EyesimoAPI#makeCharsForWholeBank(int, int)
		 */
	@Override
	public void makeCharsForWholeBank(int standardWidth, int standardHeight) {
		setStandardSizes(standardWidth, standardHeight);

		saveSettings();

		bankManager.makeCharsForWholeBank( settings.getImageBankDir(), settings.getCharsBankDir(), standardWidth, standardHeight);

		updateBankInfo();
	}



	//classify specify image / all images in directory (if file is directory)

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#classify(java.io.File)
     */
	@Override
	public AnalyzeResult classify(File imageFile) {


		return classify(imageFile, false);
	}

	public AnalyzeResult classify(File imageFile, boolean printFlag) {


		BufferedImage readedImage = bankManager.readImage(imageFile);
		String imageName = bankManager.getLastReadedImageName();

		return classify(readedImage, imageName, printFlag);
	}

	public AnalyzeResult classify(BufferedImage image, String name) {

		return classify(image, name, false);
	}

	public AnalyzeResult classify(BufferedImage image, String name, boolean printFlag) {

		BufferedImage preparedImage = CharsMaker.prepareObject(image, settings.getStWidth(), settings.getStHeight());

		AnalyzeResult result = smartProc.doAnalyze(preparedImage, name, bankInfo);

		if ( printFlag )
		System.out.println("Image: " + name + " Class: " + result.getClassName() + " ClassIndex: " + result.getClassIndex());

		return result;
	}

	@Override
	public int learnK() {

		int bankLen = bankInfo.getBankInfo().length;

		int N = 10;
		int c = bankLen / ( 10 * N);
		int maxNeighbors = bankLen > 150 ? 150 : bankLen - 1;

		return smartProc.learnParamK(bankInfo, N, c, maxNeighbors);
	}

	@Override
	public void setK(int  _k) {
		settings.setK( _k );
	}

	//read and set default settings of Eyesimo
	//rewrite default settings

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#setImageBankDir(java.io.File)
     */
	@Override
	public void setImageBankDir(File newBankDir) {
		settings.setImageBankDir( newBankDir );
	}

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#setCharsBankDir(java.io.File)
     */
	@Override
	public void setCharsBankDir(File newBankDir) {
		settings.setCharsBankDir( newBankDir );
	}

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#getCurrentK()
     */
	@Override
	public int getCurrentK() {
		return settings.getK();
	}

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#getCurrentStandardWidth()
     */
	@Override
	public int getCurrentStandardWidth() {
		return settings.getStWidth();
	}

	/* (non-Javadoc)
     * @see eyesimo.main.EyesimoAPI#getCurrentStandardHeight()
     */
	@Override
	public int getCurrentStandardHeight() {
		return settings.getStHeight();
	}

	@Override
	public void saveSettings() {
		settingsManager.writeSettings( this.settings );
	}

	@Override
	public void updateSettings() {
		settings = settingsManager.readSettings();
	}

	@Override
	public Point2D.Double[] getChartData( String choseClass) {
		return bankManager.getChartData(settings, choseClass);
	}

	@Override
	public void extractSamples( String choseClass, double marginLimit ) {
		bankManager.extractSamples( settings, choseClass, marginLimit);

		updateSettings();
	}

	@Override
	public String[] getClassNames() {

		return settings.getCharsBankDir().list();
	}


}
