package io.github.knightmareleon.features.sets.components.main;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;

public class SetsMainController implements SetsPage{

    private SetsNavigator navigator;

    private final SetsService setsService;

    public SetsMainController(AppContext context){
        this.setsService = context.getStudySetService();
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    @SuppressWarnings("unused")
    private void onCreateSetsClicked() {
        navigator.show("create");
    }

}
