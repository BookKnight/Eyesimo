package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader( getClass().getResource("ui.fxml") );

        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.initController();

        primaryStage.setTitle("Eyesimo");
        primaryStage.setScene(new Scene(root, 754, 677));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);

    }
}
