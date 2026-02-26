package io.github.knightmareleon.features.sets.components.pages;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXML;

public class SetDetailsController implements SetsPage {

    private final SetsService setsService;
    private SetsNavigator navigator;
    private StudySet studySet;

    public SetDetailsController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    public void setStudySet(StudySet studySet){
        System.out.println("Retrieved study set:\n" + studySet );
        this.studySet = studySet;
    }

    @FXML
    public void initialize(){

    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show("main");
    }
}
