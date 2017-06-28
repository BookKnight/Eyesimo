package eyesimo.bankmanager.tools;

import eyesimo.bankmanager.io.CharsReader;
import eyesimo.main.Eyesimo;
import eyesimo.processor.judgers.PafJudger;
import eyesimo.processor.segmentators.ImageFullChars;
import eyesimo.settingsmanager.EyesimoSettings;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by Anton_Erde on 15.04.2017.
 */
public class SamplesExtractor {

    // Components:

    EyesimoSettings settings;

    CharsExtractor charsExtractor;
    PafJudger pafJudger;

    // допуск
    double marginLimit;
    // имя выбранного класса
    String choseClass;
    // флаг резерного копирования
    boolean backupFlag;

    // Work data
    // база объектов выбранного класса
    CommonBankCharsInfo bankChars;


    ////////////////////////////////////
    // Методы
    ////////////////////////////////////

    // public:

    public SamplesExtractor() {

        charsExtractor = new CharsExtractor();
        pafJudger = new PafJudger();

    }

    // отобрать эталоны
    public void extractSamples( ) {

        int choseClassInd = -1;

        for (int i = 0; i < bankChars.getClassCount(); i++) {
            if ( bankChars.getClassNames()[i].equals( choseClass )) {
                choseClassInd = i;
                break;
            }
        }

        File[] charsFiles = settings.getCharsBankDir().listFiles()[choseClassInd].listFiles();

        CharsReader charsReader = new CharsReader();
        ImageFullChars[] choseClassChars = new ImageFullChars[ charsFiles.length ];
        for ( int i = 0; i < charsFiles.length; i++) {
            choseClassChars[i] = charsReader.readFullInfo(charsFiles[i]);
        }

        for (int i = 0; i < charsFiles.length; i++) {
            double margin = findMarginFor( choseClassChars[i], choseClassInd );

            if ( margin <= marginLimit ) charsFiles[i].delete();
        }

    }

    // получить данные для графика
    public Point2D.Double[] getChartData() {

        int choseClassInd = -1;

        for (int i = 0; i < bankChars.getClassCount(); i++) {
            if ( bankChars.getClassNames()[i].equals( choseClass )) {
                choseClassInd = i;
                break;
            }
        }

        int choseClassObjCount = 0;
        CurrentBankCharsInfo[] bankCharsInfos = bankChars.getBankInfo();
        for (int i = 0; i < bankCharsInfos.length; i++) {
            if ( bankCharsInfos[i].classIndex == choseClassInd ) choseClassObjCount++;
        }


        CurrentBankCharsInfo[] choseClassChars = new CurrentBankCharsInfo[choseClassObjCount];

        int j = 0;
        for ( int i = 0; i < bankCharsInfos.length; i++) {
            if ( bankCharsInfos[i].getClassIndex() == choseClassInd ) {
                choseClassChars[j] = bankCharsInfos[i];
                j++;
            }
        }


        Vector<Double> marginVec = new Vector<>(choseClassObjCount);
        for (int i = 0; i < choseClassObjCount; i++) {
            double tmpMargin = findMarginFor(choseClassChars[i].getBankChars(), choseClassInd);

            marginVec.add(tmpMargin);
        }

        marginVec.sort( Double::compareTo );

        Point2D.Double[] chartsPoints = new Point2D.Double[choseClassObjCount];

        for (int i = 0; i < choseClassObjCount; i++)
            chartsPoints[i] = new Point2D.Double(i + 1, marginVec.get(i));


        return chartsPoints;
    }


    //установить настройки
    public void setSettings(EyesimoSettings _settings) {
        settings = _settings;

        bankChars = charsExtractor.getBankChars( settings.getCharsBankDir() );
    }

    // установить допуск
    public void setMarginLimit( double _marginLimit ) {
        marginLimit = _marginLimit;
    }

    // установить имя выбранного класса
    public void setChoseClass( String _choseClass ) {
        choseClass = _choseClass;
    }


    // установить флаг резервного копирования
    public void setBackupFlag( boolean _backupFlag ) {
        backupFlag = _backupFlag;
    }

    // private:

    // найти отступ

    private double findMarginFor( ImageFullChars object, int objectClassInd ) {

        // rating of object in own class
        double ownRating = 0;
        // rating of object in other class;
        double otherRating = Double.MIN_VALUE;

        ownRating = pafJudger.getRatingOfNeighborhood(object, bankChars, objectClassInd, settings.getK());

        int classCount = bankChars.getClassCount();
        for (int i = 0; i < classCount; i++) {

            if ( i != objectClassInd ) {

                double tmpRating = pafJudger.getRatingOfNeighborhood(object, bankChars, i, settings.getK());

                if ( otherRating <= tmpRating ) otherRating = tmpRating;
            }
        }

        return ownRating - otherRating;
    }

    // удалить объект из банка

}
