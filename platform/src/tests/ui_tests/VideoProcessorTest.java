package tests.ui_tests;

import ui.videoRecognition.VideoProcessor;

/**
 * Created by Anton_Erde on 04.04.2017.
 */
public class VideoProcessorTest {

    public static void main(String args[]) {

        VideoProcessor videoProcessor = new VideoProcessor();

        videoProcessor.setVideoPath( "\\media\\antonerde\\MyData1\\Downloads\\browser\\Top 10 Most Expensive Yachts In The World (1).mp4");

        videoProcessor.initPlayer();

        while ( videoProcessor.isPlaying() ) {
            videoProcessor.classifyNextFrame();
        }
    }
}
