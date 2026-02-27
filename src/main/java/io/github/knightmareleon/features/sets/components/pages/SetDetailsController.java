package io.github.knightmareleon.features.sets.components.pages;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetDetailsController implements SetsPage {

    private final SetsService setsService;
    private SetsNavigator navigator;
    private StudySet studySet;

    @FXML private Label setName;
    @FXML private Label subjectLabel;
    @FXML private Label createdOnLabel;
    @FXML private Label lastTakeOnLabel;

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
        this.setName.setText(this.studySet.getTitle());
        this.subjectLabel.setText(this.studySet.getSubject());
        this.createdOnLabel.setText(this.studySet.getDateCreatedOn().toString());
        this.lastTakeOnLabel.setText(this.studySet.getDataLastTakeOn() == null ? 
            "Not yet taken." : this.studySet.getDataLastTakeOn().toString());
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
