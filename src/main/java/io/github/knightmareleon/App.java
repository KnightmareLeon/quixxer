package io.github.knightmareleon;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.database.DatabaseManager;
import io.github.knightmareleon.shared.infrastructure.database.SchemaInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception{

        Connection connection = DatabaseManager.getConnection();
        SchemaInitializer.initialize();

        Font.loadFont(getClass().getResourceAsStream("/io/github/knightmareleon/fonts/Inter_24pt-ExtraLight.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("/io/github/knightmareleon/fonts/Inter-VariableFont_opsz,wght.ttf"), 16);

        AppContext context = new AppContext(connection);

        FXMLLoader rootLoader = new FXMLLoader(
            getClass().getResource("/io/github/knightmareleon/RootView.fxml")
        );

        rootLoader.setControllerFactory(type -> {

            if (type == RootController.class) {
                return new RootController(context);
            }

            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = rootLoader.load();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        Scene scene = new Scene(root, width - 50, height - 100);

        scene.getStylesheets().add(
            getClass().getResource("/io/github/knightmareleon/css/base.css").toExternalForm()
        );
        scene.getStylesheets().add(
            getClass().getResource("/io/github/knightmareleon/css/controls.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Quixxer");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
