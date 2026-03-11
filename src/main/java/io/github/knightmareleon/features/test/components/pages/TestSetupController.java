package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.TestTypeReceiver;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestSetupController implements TestPage, StudySetReceiver, TestTypeReceiver{

    private TestNavigator testNavigator;
    private TestType testType;
    private StudySet studySet;

    @FXML private Label setupTitle;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        System.out.println("Preparing study set " + this.studySet.getTitle());
    }

    @Override
    public void receiveTestType(TestType testType) {
        this.testType = testType;
        this.setupTitle.setText(testType.getName() + " Setup");
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.testNavigator.show(TestPageURL.SETS, this.testType);
    }
}
