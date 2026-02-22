package io.github.knightmareleon.shared.ui.controls;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class IconButton extends Button{

    @FXML
    private FontIcon icon = new FontIcon();

    private final String iconLiteral;
    private final String iconStyleClass;

    @SuppressWarnings("LeakingThisInConstructor")
    public IconButton(@NamedArg("iconLiteral") String iconLiteral, @NamedArg("iconStyleClass") String iconStyleClass) {
        this.iconLiteral = iconLiteral;
        this.iconStyleClass = iconStyleClass;

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
        this.icon.setIconLiteral(this.iconLiteral);
        this.icon.getStyleClass().add(this.iconStyleClass);
        this.setGraphic(this.icon);
    }

    public void setOnAction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}