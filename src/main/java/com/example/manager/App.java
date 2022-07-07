package com.example.manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends Application {
    public static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        scene.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
        stage.setTitle("Password Manager");
        stage.setMinWidth(638);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.getIcons().add(new Image("lock.png"));

        stage.show();

    }

    public static String dateSave(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        return "Last logged in: " + formatter.format(date);
    }
    public  void changeStage() throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("manager-view.fxml"));
        stg.getScene().setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}