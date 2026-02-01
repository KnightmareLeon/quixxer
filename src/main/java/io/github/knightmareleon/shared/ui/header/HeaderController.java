package io.github.knightmareleon.shared.ui.header;

import java.util.function.Consumer;

import javafx.fxml.FXML;

public class HeaderController {

    private Consumer<String> onTabSelected;

    public void setOnTabSelected(Consumer<String> handler) {
        this.onTabSelected = handler;
    }

    @FXML
    private void onDashboardClicked() {
        onTabSelected.accept("dashboard");
    }

    @FXML
    private void onTestClicked() {
        onTabSelected.accept("test");
    }

    @FXML
    private void onSetsClicked() {
        onTabSelected.accept("sets");
    }

    @FXML
    private void onSettingsClicked() {
        onTabSelected.accept("settings");
    }
}
