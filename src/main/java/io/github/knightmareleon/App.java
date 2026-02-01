package io.github.knightmareleon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader rootLoader = new FXMLLoader(
            getClass().getResource("/io/github/knightmareleon/RootView.fxml")
        );

        Parent root = rootLoader.load();

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
            getClass().getResource("/io/github/knightmareleon/css/base.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Quixxer");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
