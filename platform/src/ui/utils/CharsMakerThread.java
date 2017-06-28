package ui.utils;

import eyesimo.main.Eyesimo;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by Anton_Erde on 21.03.2017.
 */
public class CharsMakerThread extends Thread{

    Eyesimo eyesimo;
    Recorder recorder;

    String imageDirPath;
    String charsDirPath;

    int stW;
    int stH;

    boolean imageDirFlag = false;
    boolean charsDirFlag = false;

    public CharsMakerThread(Recorder _recorder, int _stW, int _stH) {
        eyesimo = new Eyesimo();
        recorder = _recorder;

        imageDirPath = null;
        charsDirPath = null;

        stW = _stW;
        stH = _stH;
    }

    @Override
    public void run() {

        PrintStream stOut = System.out;
        System.setOut( new PrintStream( recorder, true));

        long startTime = System.currentTimeMillis();

        if ( imageDirFlag && charsDirFlag ) {

            File imageDir = new File( imageDirPath );
            File charsDir = new File( charsDirPath );

            eyesimo.makeChars( imageDir, charsDir, stW, stH );
        } else {

            eyesimo.makeCharsForWholeBank(stW, stH);
            eyesimo.saveSettings();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) / 1000 + " sec");

        System.setOut(stOut);

    }

    public void setImageDirPath( String _imageDirPath ) {
        imageDirPath = _imageDirPath;

        imageDirFlag = true;
    }

    public  void setCharsDirPath( String _charsDirPath ) {
        charsDirPath = _charsDirPath;

        charsDirFlag = true;
    }
}
