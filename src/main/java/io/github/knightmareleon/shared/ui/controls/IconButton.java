package io.github.knightmareleon.shared.ui.controls;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class IconButton extends Button{

    @FXML private Label iconLabel;
    @FXML private Label textLabel;

    private final StringProperty icon = new SimpleStringProperty();

    public IconButton() {
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

        textLabel.textProperty().bind(textProperty());
        iconLabel.textProperty().bind(this.icon);
    }

    public StringProperty iconProperty() {
        return this.icon;
    }

    public void setIcon(String value) {
        this.icon.set(value);
    }

    public String getIcon() {
        return this.icon.get();
    }
}
