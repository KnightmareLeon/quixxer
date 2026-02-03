package io.github.knightmareleon.shared.ui.controls;

import java.io.IOException;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class IconButton extends Button{

    @FXML
    private Region icon = new Region();

    private final String iconStyleClass;
    private final String iconPath;

    public IconButton(@NamedArg("iconStyleClass") String iconStyleClass, @NamedArg("iconPath") String iconPath) {
        this.iconStyleClass = iconStyleClass;
        this.iconPath = iconPath;
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("IconButton.fxml")
        );
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        //icon.setClass("-fx-background-color: white; -fx-pref-width: 1.333em;  -fx-pref-height: 1.333em; -fx-shape: \"m 0,15.984 5.672,5.664 c 0,0 3.182,-3.18 6.314,-6.312 V 32 h 8.021 V 15.336 l 6.32,6.32 L 32,15.984 16.002,0 Z\";");
        icon.getStyleClass().add(this.iconStyleClass);
        icon.setStyle("-fx-shape: \"" + this.iconPath + "\"");
        this.setGraphic(icon);
    }

}