package io.github.knightmareleon.features.sets.components.create;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import javafx.fxml.FXML;

public class SetsCreateController implements SetsPage{

    private SetsNavigator navigator;

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    private void onBackPageClick() {
        navigator.show("main");
    }

}
