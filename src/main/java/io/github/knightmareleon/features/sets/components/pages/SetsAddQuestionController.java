package io.github.knightmareleon.features.sets.components.pages;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.controls.QuestionField;
import io.github.knightmareleon.features.sets.constants.SetsPageURL;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetsAddQuestionController implements SetsPage, StudySetReceiver{

    private SetsNavigator navigator;
    private StudySet studySet;
    
    @FXML private QuestionField questionField;
    @FXML private Label addQuestionTitle;

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet){
        this.studySet = studySet;
        this.initWithStudySet();
    }

    private void initWithStudySet(){
        this.addQuestionTitle.setText(
            "Add New Question to " + this.studySet.getTitle()
        );

    }
    @FXML
    public void initialize(){
        this.questionField.setClosable(false);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show(SetsPageURL.DETAILS, this.studySet);
    }
}
