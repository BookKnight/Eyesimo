package ui.utils;

import eyesimo.main.Eyesimo;
import eyesimo.processor.judgers.AnalyzeResult;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by Anton_Erde on 21.03.2017.
 */
public class ClassificationThread extends Thread {

    Eyesimo eyesimo;
    Recorder recorder;

    File imageFile;

    AnalyzeResult analyzeResult;

    TableView imageTable;

    int kParam;

    public ClassificationThread(Recorder _recorder, TableView _imageTable, int _kParam) {
        eyesimo = new Eyesimo();
        recorder = _recorder;

        imageFile = null;

        analyzeResult = null;

        imageTable = _imageTable;

        kParam = _kParam;
        eyesimo.setK( kParam );
    }

    @Override
    public void run() {

        PrintStream stOut = System.out;
        System.setOut( new PrintStream( recorder, true));

        ObservableList<ImageTableValue> tableData = imageTable.getItems();

        File imageFile = null;
        for (int i = 0; i < tableData.size(); i++) {

            imageFile = new File(tableData.get(i).getImagePath());

            analyzeResult = eyesimo.classify(imageFile, true);

            UpdateThread updateThread = new UpdateThread(i, analyzeResult, imageTable, tableData);

            Platform.runLater( updateThread );
        }

        System.setOut(stOut);
    }

    public AnalyzeResult getAnalyzeResult() {
        return  analyzeResult;
    }
}
