package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/pingPong.fxml"));
        primaryStage.setTitle("Ping-pong");
        primaryStage.setScene(new Scene(root, 568, 345));
        primaryStage.show();

        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}