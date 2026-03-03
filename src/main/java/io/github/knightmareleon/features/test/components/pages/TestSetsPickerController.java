package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestNavigator;
import javafx.fxml.FXML;

public class TestSetsPickerController implements TestPage{
    TestNavigator navigator;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.navigator = testNavigator;
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show("main");
    }
}
