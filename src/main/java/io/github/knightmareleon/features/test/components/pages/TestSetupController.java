package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.TestTypeReceiver;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.NaturalNumberField;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class TestSetupController implements TestPage, StudySetReceiver, TestTypeReceiver{

    private TestNavigator testNavigator;
    private TestType testType;
    private StudySet studySet;

    @FXML private Label setupTitle;
    @FXML private NaturalNumberField totalQuestions;
    @FXML private Label totalQuestionsMax;

    @FXML private ToggleButton timeToggleButton;
    @FXML private ToggleButton thirtySecButton;
    @FXML private ToggleButton oneMinButton;
    @FXML private ToggleButton threeMinButton;
    @FXML private ToggleButton fiveMinButton;
    @FXML private ToggleButton tenMinButton;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        int maxTotalQuestions = this.studySet.getQuestions().size();
        this.totalQuestions.setMaxNumber(maxTotalQuestions);
        this.totalQuestionsMax.setText(" / " + maxTotalQuestions);
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

    @FXML
    public void initialize(){
        this.timeToggleButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            this.onTimeSelected(oldVal);
        });

        this.onTimeSelected(!this.timeToggleButton.isSelected());

        ToggleGroup timeToggleGroup = new ToggleGroup();
        thirtySecButton.setToggleGroup(timeToggleGroup);
        oneMinButton.setToggleGroup(timeToggleGroup);
        threeMinButton.setToggleGroup(timeToggleGroup);
        fiveMinButton.setToggleGroup(timeToggleGroup);
        tenMinButton.setToggleGroup(timeToggleGroup);
    }

    @FXML
    private void onTimeSelected(boolean selected){
        this.thirtySecButton.setDisable(selected);
        this.oneMinButton.setDisable(selected);
        this.threeMinButton.setDisable(selected);
        this.fiveMinButton.setDisable(selected);
        this.tenMinButton.setDisable(selected);
    }
}