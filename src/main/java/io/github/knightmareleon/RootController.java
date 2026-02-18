package io.github.knightmareleon;

import io.github.knightmareleon.shared.navigator.RootTabNavigator;
import io.github.knightmareleon.shared.ui.header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class RootController {

    @FXML
    private StackPane contentArea;

    @FXML
    private HeaderController headerController;

    private RootTabNavigator navigator;

    @FXML
    public void initialize() {

        navigator = new RootTabNavigator(contentArea);

        headerController.setOnTabSelected(tabId -> {
            navigator.show(tabId);
        });

        navigator.show("dashboard");
    }
}
