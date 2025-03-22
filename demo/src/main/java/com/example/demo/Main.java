package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/demo/demo.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Калькулятор");
        stage.setScene(scene);

        scene.getStylesheets().add(getClass().getResource("/com/example/demo/style.css").toExternalForm());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
