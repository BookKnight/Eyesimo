package ui.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Anton_Erde on 18.03.2017.
 */
public class Recorder extends OutputStream{

    private TextArea record;
    private int charsCount;

    public Recorder( TextArea context ) {

        record = context;

        charsCount = 0;
    }

    @Override
    synchronized public void write( int b ) throws IOException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                synchronized (record) {

                    if ( charsCount == 30000) {
                        record.clear();
                        charsCount = 0;
                    }

                    record.appendText(String.valueOf((char) b));
                    charsCount++;
                }
            }
        });
    }
}
