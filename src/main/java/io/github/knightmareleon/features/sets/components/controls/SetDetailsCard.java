package io.github.knightmareleon.features.sets.components.controls;

import io.github.knightmareleon.shared.utils.ControllerRootSetter;
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

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("SetDetailsCard.fxml")
        );
        ControllerRootSetter.set(this, loader);
    }

    @FXML
    public void initialize() {
        this.headerLabel.setText(this.headerText);
    }

    public void setDataText(String dataText) {
        this.dataLabel.setText(dataText);
    }
}
