package io.github.knightmareleon.shared.ui.controls;

import javafx.scene.control.Alert;

public class StandardAlert extends Alert {
    
    public StandardAlert(AlertType at) {
        super(at);
        this.getDialogPane().getStylesheets().add(
                getClass().getResource("/io/github/knightmareleon/css/base.css").toExternalForm()
        );
        this.getDialogPane().getStyleClass().add("standard-alert");
    }
    
}
