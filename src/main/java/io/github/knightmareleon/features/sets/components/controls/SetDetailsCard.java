package io.github.knightmareleon.features.sets.components.controls;

import java.io.IOException;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SetDetailsCard extends VBox{

    @FXML private Label headerLabel;
    @FXML private Label dataLabel;

    private final String headerText;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetDetailsCard(@NamedArg("headerText") String headerText) {

        this.headerText = headerText;

        FXMLLoader loader;
        loader = new FXMLLoader(
                getClass().getResource("SetDetailsCard.fxml")
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
        this.headerLabel.setText(this.headerText);
    }

    public void setDataText(String dataText) {
        this.dataLabel.setText(dataText);
    }
}
