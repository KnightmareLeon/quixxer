package io.github.knightmareleon.features.settings;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;

public class SettingsController {
    private final AppContext context;

    public SettingsController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        System.out.println("Settings loaded");
    }
}