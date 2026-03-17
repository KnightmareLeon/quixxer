package io.github.knightmareleon.shared.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class ControllerRootSetter {
    public static void set(Object object, FXMLLoader loader){

        loader.setRoot(object);
        loader.setController(object);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
