package ui.videoRecognition;

import eyesimo.processor.judgers.AnalyzeResult;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import ui.utils.Recorder;

import java.io.PrintStream;

/**
 * Created by Anton_Erde on 04.04.2017.
 */
public class VideoProcessorThread extends Thread {

    VideoProcessor videoProcessor;
    Recorder recorder;
    TextField fpsField;

    public VideoProcessorThread(Recorder _recorder, TextField _fpsField, TextField videoPath) {

        videoProcessor = new VideoProcessor();

        videoProcessor.setVideoPath( videoPath.getText() );

        recorder = _recorder;
        fpsField = _fpsField;
    }

    @Override
    public void run() {


        PrintStream stOut = System.out;
        System.setOut( new PrintStream( recorder, true));


        PrintStream printStream = new PrintStream( recorder, true );

        videoProcessor.initPlayer();

        while ( videoProcessor.isPlaying() ) {

            videoProcessor.classifyNextFrame();

            if ( videoProcessor.getFrameCount() % 32 == 0) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        videoProcessor.printFPS( fpsField );
                    }
                });
            }

            if ( videoProcessor.getFrameCount() % 100 == 0) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        videoProcessor.updateCounts();
                    }
                });
            }
        }

        System.setOut(stOut);

    }
}
