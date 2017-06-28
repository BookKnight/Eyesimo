package ui;

import eyesimo.main.Eyesimo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ui.utils.*;
import ui.videoRecognition.VideoProcessorThread;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class Controller {

    ///////////////////////////////////////////
    //Video Recognition
    ///////////////////////////////////////////

    @FXML
    Button chooseVideoButton;

    @FXML
    TextField videoFilePathField;

    @FXML
    Button beginVideoRecog;

    @FXML
    TextField fpsField;

    @FXML
    TextArea videoRecogArea;

    ///////////////////////////////////////////
    //Image Recognition
    ///////////////////////////////////////////

    @FXML
    TableView imageTable;

    @FXML
    Button addImagesButton;

    @FXML
    TextField kParam;

    @FXML
    TextField findWidth;

    @FXML
    TextField findHeight;

    @FXML
    TextArea findLogArea;

    @FXML
    Button beginFindButton;


    /////////////////////////////////
    //Characteristics manager
    /////////////////////////////////

    @FXML
    RadioButton optionalDir;

    @FXML
    RadioButton defaultDir;

    @FXML
    TextField optionalImageDirPath;

    @FXML
    TextField optionalCharsDirPath;

    @FXML
    Button setOptionalImageDirButton;

    @FXML
    Button setOptionalCharsDirButton;

    @FXML
    TextField standardWidth;

    @FXML
    TextField standardHeight;

    @FXML
    Button beginMakeCharsButton;

    @FXML
    TextArea charsMakerLogArea;

    ////////////////////////////////////
    //Learn tab
    ////////////////////////////////////

    @FXML
    private CheckBox learnKCheckBox;

    @FXML
    private CheckBox saveKCheckBox;

    @FXML
    private TextArea learningLogArea;

    @FXML
    private Button beginLearnButton;

    ////////////////////////////////////
    //Sample extraction
    ////////////////////////////////////

    @FXML
    ScatterChart<Number, Number> chartPane;

    @FXML
    ChoiceBox<String> classChoiceBox;

    @FXML
    Button drawChartButton;

    @FXML
    TextField marginLimitField;

    @FXML
    Button extractSamplesButton;



    /////////////////////////////////////////
    //Methods
    /////////////////////////////////////////

    public Controller() {
    }


    public void initController() {

        Eyesimo eyesimo = new Eyesimo();

        Integer kNeighbours = eyesimo.getCurrentK();

        kParam.appendText( kNeighbours.toString() );

        findWidth.appendText( new Integer( eyesimo.getCurrentStandardWidth()).toString() );

        findHeight.appendText( new Integer( eyesimo.getCurrentStandardHeight()).toString() );

        classChoiceBox.setItems( FXCollections.observableArrayList( eyesimo.getClassNames() ) );

    }

    ///////////////////////////////////////////
    //Video Recognition
    ///////////////////////////////////////////

    @FXML
    private void chooseVideoFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory( new File ("./"));

        Window ownerWindow = setOptionalImageDirButton.getScene().getWindow();

        File chosedVideo = fileChooser.showOpenDialog( ownerWindow );

        videoFilePathField.clear();
        videoFilePathField.appendText( chosedVideo.getAbsolutePath() );
    }

    @FXML
    private void startVideoRecog() {

        if ( !videoFilePathField.getText().equals("") ) {

            Recorder recorder = new Recorder(videoRecogArea);

            VideoProcessorThread videoProcessorThread = new VideoProcessorThread( recorder, fpsField, videoFilePathField);

            videoProcessorThread.start();
        }

    }

    ///////////////////////////////////////////
    //Recognition
    ///////////////////////////////////////////

    @FXML
    private void beginClassification() {

        Recorder recorder = new Recorder( findLogArea );
        int kNeighbours = Integer.parseInt( kParam.getText() );

        ClassificationThread classificationThread = new ClassificationThread( recorder, imageTable, kNeighbours );

        classificationThread.start();
    }

    @FXML
    private void addImages() {

        initImageTableProperties();

        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory( new File("./"));

        Window windowOwner = addImagesButton.getScene().getWindow();

        List<File> imageFiles = fileChooser.showOpenMultipleDialog( windowOwner );

        ObservableList<ImageTableValue> data = FXCollections.observableArrayList();

        data.addAll(imageTable.getItems());

        for (int i = 0; i < imageFiles.size(); i++) {

            String imageName = imageFiles.get(i).getName();
            String imageClass = "no info"; //not classificated image
            String path = imageFiles.get(i).getAbsolutePath();

            data.add( new ImageTableValue( imageName, imageClass, path));
        }

        imageTable.setItems( data );
    }



    @FXML
    private void initImageTableProperties() {

        ObservableList<TableColumn> imageColumns = imageTable.getColumns();

        imageColumns.get(0).setCellValueFactory( new PropertyValueFactory<>( "imageName" ));
        imageColumns.get(1).setCellValueFactory( new PropertyValueFactory<>( "imageClass" ));
        imageColumns.get(2).setCellValueFactory( new PropertyValueFactory<>( "imagePath" ));

    }

    @FXML
    private void clearTable() {
        ObservableList<ImageTableValue> tableData = imageTable.getItems();
        tableData.clear();
        imageTable.setItems(tableData);
    }

    @FXML
    private void clearLog() {
        findLogArea.clear();
    }

    ///////////////////////////////////////////
    //Characteristics manager
    ///////////////////////////////////////////

    @FXML
    private void makeChars() {

        Recorder charsLog = new Recorder( charsMakerLogArea );
        PrintStream stOut = System.out;
        System.setOut( new PrintStream( charsLog, true) );

        if ( optionalDir.isSelected() ) {

            String optImgPath = optionalImageDirPath.getText();
            String optChrsPath = optionalCharsDirPath.getText();

            if (optImgPath.isEmpty()) {
                System.out.println( "Image catalog path is incorrect.");
                return;
            }

            if (optChrsPath.isEmpty()) {
                System.out.println( "Chars catalog path is incorrect.");
                return;
            }
        }

        if ( standardWidth.getText().isEmpty()) {
            System.out.println( "Standard width is incorrect.");
            return;
        }

        if ( standardHeight.getText().isEmpty()) {
            System.out.println( "Standard height is incorrect.");
            return;
        }

        int stW = Integer.parseInt( standardWidth.getText() );
        int stH = Integer.parseInt( standardHeight.getText() );

        if ( optionalDir.isSelected() ) {
            CharsMakerThread charsMakerThread = new CharsMakerThread( charsLog, stW, stH );
            charsMakerThread.setImageDirPath( optionalImageDirPath.getText() );
            charsMakerThread.setCharsDirPath( optionalCharsDirPath.getText() );

            charsMakerThread.start();
        }

        if ( defaultDir.isSelected() ) {
            CharsMakerThread charsMakerThread = new CharsMakerThread( charsLog, stW, stH );

            charsMakerThread.start();
        }

        System.setOut( stOut );
    }

    @FXML
    private void setOptionalDir() {

        defaultDir.setSelected(false);

        optionalImageDirPath.clear();
        optionalImageDirPath.setEditable( true );

        optionalCharsDirPath.clear();
        optionalCharsDirPath.setEditable( true );

        setOptionalImageDirButton.setDisable(false);

        setOptionalCharsDirButton.setDisable(false);

    }

    @FXML
    private void setDefaultDir() {
        optionalDir.setSelected(false);

        optionalImageDirPath.clear();
        optionalImageDirPath.setEditable( false );

        optionalCharsDirPath.clear();
        optionalCharsDirPath.setEditable( false );

        setOptionalImageDirButton.setDisable(true);

        setOptionalCharsDirButton.setDisable(true);
    }

    @FXML
    private void setOptionalImagePath() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory( new File ("./"));

        Window ownerWindow = setOptionalImageDirButton.getScene().getWindow();

        File chosedImageDir = directoryChooser.showDialog( ownerWindow );

        optionalImageDirPath.clear();
        optionalImageDirPath.appendText( chosedImageDir.getAbsolutePath() );
    }

    @FXML
    private void setOptionalCharsPath() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory( new File ("./"));

        Window ownerWindow = setOptionalCharsDirButton.getScene().getWindow();

        File chosedImageDir = directoryChooser.showDialog(ownerWindow);

        optionalCharsDirPath.clear();
        optionalCharsDirPath.appendText( chosedImageDir.getAbsolutePath() );

    }


    ///////////////////////////////////////
    //Learning
    ///////////////////////////////////////

    @FXML
    private void beginLearn() {

        learningLogArea.clear();

        boolean learnKFlag = false;
        learnKFlag = learnKCheckBox.isSelected();

        boolean saveFlag = false;
        saveFlag = saveKCheckBox.isSelected();

        //if nothing selected
        if ( learnKFlag == false ) {
            return;
        }

        //begin learning

        Recorder recorder = new Recorder(learningLogArea);


        LearnThread learnThread = new LearnThread(recorder, saveFlag);
        learnThread.start();
    }

    ////////////////////////////////////
    //Sample extraction
    ////////////////////////////////////

    @FXML
    private void drawChart() {

        chartPane.getData().clear();

        Eyesimo eyesimo = new Eyesimo();

        long startTime = System.currentTimeMillis();

        String choseClass = classChoiceBox.getValue();
        Point2D.Double[] chartData = eyesimo.getChartData( choseClass );

        XYChart.Series<Number, Number> marginSeries = new <Number, Number>XYChart.Series();

        for (int i = 0; i < chartData.length; i++) {
            marginSeries.getData().add( new XYChart.Data<>( chartData[i].getX(), chartData[i].getY()));
        }

        chartPane.autosize();

        chartPane.getData().addAll(marginSeries);


        long endTime = System.currentTimeMillis();
        System.out.println("Time draw " + chartData.length + " elements: " + (endTime - startTime) / 1000 + " sec");
    }

    @FXML
    private void extractSamples() {

        double marginLimit = Double.parseDouble( marginLimitField.getText() );
        String choseClass  = classChoiceBox.getValue();

        Eyesimo eyesimo = new Eyesimo();

        long startTime = System.currentTimeMillis();

        eyesimo.extractSamples( choseClass, marginLimit );

        long endTime = System.currentTimeMillis();
        System.out.println("Time extract: " + (endTime - startTime) / 1000 + " sec");
    }

}
