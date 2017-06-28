package ui.utils;

import eyesimo.main.Eyesimo;
import javafx.scene.control.TextArea;

import java.io.PrintStream;

/**
 * Created by Anton_Erde on 18.03.2017.
 */
public class LearnThread extends Thread {

    Eyesimo eyesimo;
    Recorder recorder;

    boolean saveFlag;
    boolean finishedFlag;

    public LearnThread(Recorder _recorder, boolean _saveFlag) {
        eyesimo = new Eyesimo();
        recorder = _recorder;

        saveFlag = _saveFlag;
        finishedFlag = false;
    }

    @Override
    public void run() {

        PrintStream stOut = System.out;
        System.setOut( new PrintStream( recorder, true));

        int learnedK = eyesimo.learnK();

        if ( saveFlag ) {

            eyesimo.setK( learnedK );
            eyesimo.saveSettings();
        }

        System.setOut(stOut);

    }
}
