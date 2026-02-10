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
        icon.getStyleClass().add(this.iconStyleClass);
        icon.setStyle("-fx-shape: \"" + this.iconPath + "\"");
        this.setGraphic(icon);
    }

}