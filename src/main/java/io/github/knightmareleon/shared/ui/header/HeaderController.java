package io.github.knightmareleon.shared.ui.header;

import java.util.function.Consumer;

import io.github.knightmareleon.shared.constants.MainPageURL;
import io.github.knightmareleon.shared.constants.PageURL;
import javafx.fxml.FXML;

public class HeaderController {

    private Consumer<PageURL> onTabSelected;

    public void setOnTabSelected(Consumer<PageURL> handler) {
        this.onTabSelected = handler;
    }

    @FXML
    @SuppressWarnings("unused")
    private void onDashboardClicked() {
        onTabSelected.accept(MainPageURL.DASHBOARD);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onTestClicked() {
        onTabSelected.accept(MainPageURL.TEST);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onSetsClicked() {
        onTabSelected.accept(MainPageURL.SETS);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onSettingsClicked() {
        onTabSelected.accept(MainPageURL.SETTINGS);
    }
}
