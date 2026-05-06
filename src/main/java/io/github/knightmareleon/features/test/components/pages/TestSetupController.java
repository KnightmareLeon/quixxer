package io.github.knightmareleon.features.test.components.pages;

import java.util.Optional;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.controls.TestGeneralSettings;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.models.TestConfig;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class TestSetupController implements TestPage, StudySetReceiver{

    protected TestNavigator testNavigator;
    private StudySet studySet;

    @FXML private TestGeneralSettings generalSettings;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        this.generalSettings.setMaxTotalQuestions(this.studySet.getQuestions().size());
    }

    @FXML
    protected abstract void onBackPageClicked();

    @FXML
    protected abstract void onStartClicked();

    protected final TestConfig.Builder createGeneralTestConfig(TestType testType){
        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start");
        alert.setHeaderText("Starting Test for " + this.studySet.getTitle());
        alert.setContentText("Are you sure you want to start?");
        Optional<ButtonType> alertResult = alert.showAndWait();
        if(alertResult.isPresent() && ButtonType.OK == alertResult.get()){
            return new TestConfig.Builder(
                    testType,
                    this.studySet,
                    this.generalSettings.totalQuestions(),
                    this.generalSettings.timeToggled(),
                    this.generalSettings.timeSetting(),
                    this.generalSettings.shuffleToggled(),
                    this.generalSettings.continuousToggled()
            );
        }
        return null;
    }
}