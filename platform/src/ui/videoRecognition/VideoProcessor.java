package ui.videoRecognition;

//import com.sun.deploy.trace.FileTraceListener;
import eyesimo.main.Eyesimo;
import eyesimo.processor.judgers.AnalyzeResult;
import javafx.scene.control.TextField;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Anton_Erde on 03.04.2017.
 */
public class VideoProcessor {

    private Eyesimo eyesimo;
    private String workDir;
    private String framesDir;
    private Process playingVideo;

    //Work data
    private long middleTime;
    private long currentPosition;

    int frameCount;
    long startTime;
    long endTime;

    int readedMillis;

    public VideoProcessor() {

        eyesimo = new Eyesimo();
        workDir = "./video-subsystem/";
        framesDir = workDir + "frames/";

        frameCount = 0;
        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
    }


    //sets paths in components too
    public void setVideoPath(String videoFilePath){

        try {

            File settingsFile = new File(workDir + "settings.txt");

            FileWriter writer = new FileWriter( settingsFile, false);

            writer.write(videoFilePath + "\r\n");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //just begin play video
    public void initPlayer(){

        ProcessBuilder processBuilder = new ProcessBuilder( );
        processBuilder.command("cmd.exe", "/K", "start & cd video-subsystem &  videoRecog.exe & exit");

        try {
            playingVideo = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void classifyNextFrame(){

        AnalyzeResult analyzeResult = new AnalyzeResult("no data", -1);

        File framesCat = new File (framesDir);
        File[] imageFileList = framesCat.listFiles();

        if ( imageFileList.length == 1) {

            File imageFile = imageFileList[0];
            String imageFileName = imageFile.getName();

            if (imageFile.exists()) {

                try {
                    BufferedImage readedImage = ImageIO.read(imageFile);

                    if (readedImage != null) {
                        readedMillis = getMillisFromFileName(imageFileName);

                        analyzeResult = eyesimo.classify(readedImage, imageFileName);

                        printResults(analyzeResult);

                        frameCount++;

                        imageFile.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public int getMillisFromFileName( String fileName) {

        int ind = fileName.indexOf(".");
        String parseMillis = fileName.substring(0, ind);

        return Integer.parseInt(parseMillis);
    }

    public  int[] playingTime( ){

        int sec = ( readedMillis / 1000 ) % 60;
        int min = (( readedMillis / 1000) / 60) % 60;
        int hours = ((( readedMillis / 1000) / 60) / 60) % 24;

        int[] time = {hours, min, sec};

        return time;

    }

    public void printResults( AnalyzeResult analyzeResult) {

        int[] time = playingTime();
        String className = analyzeResult.getClassName();
        int classInd = analyzeResult.getClassIndex();

        String timeString = "Time: " + time[0] + ":" + time[1] + ":" + time[2] + " ";
        String infoString = " | Object class: " + className + " | Class index: " + classInd;
        StringBuilder outString = new StringBuilder( timeString + infoString + "\n");

        System.out.print( outString );
    }

    public void printFPS() {

        endTime = System.currentTimeMillis();

        long sec = (endTime - startTime) / 1000;

        if ( sec != 0 ) System.out.println( "fps: " + frameCount /  sec );
    }

    public void printFPS(TextField context) {

        endTime = System.currentTimeMillis();

        long sec = (endTime - startTime) / 1000;

        if ( sec != 0 )  {

            context.clear();
            context.appendText( Long.toString( frameCount /  sec ) );
        }
    }

    public void updateCounts() {
        frameCount = 0;

        startTime = System.currentTimeMillis();
    }

    public boolean isPlaying() {
        return playingVideo.isAlive();
    }

    public int getFrameCount() {
        return frameCount;
    }

}
