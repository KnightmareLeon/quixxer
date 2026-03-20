package io.github.knightmareleon.features.test.components.pages;

import java.util.Optional;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.TestTypeReceiver;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.models.TestData;
import io.github.knightmareleon.shared.ui.controls.NaturalNumberField;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.Converter;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

    private final ToggleGroup timeToggleGroup = new ToggleGroup();
    @FXML private ToggleButton shuffleToggleButton;
    @FXML private ToggleButton continuousToggleButton;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        int maxTotalQuestions = this.studySet.getQuestions().size();
        this.totalQuestions.setMaxNumber(maxTotalQuestions);
        this.totalQuestions.setTextToMax();
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

        thirtySecButton.setToggleGroup(this.timeToggleGroup);
        oneMinButton.setToggleGroup(this.timeToggleGroup);
        threeMinButton.setToggleGroup(this.timeToggleGroup);
        fiveMinButton.setToggleGroup(this.timeToggleGroup);
        tenMinButton.setToggleGroup(this.timeToggleGroup);
        timeToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });
        thirtySecButton.setSelected(true);
    }

    private void onTimeSelected(boolean selected){
        this.thirtySecButton.setDisable(selected);
        this.oneMinButton.setDisable(selected);
        this.threeMinButton.setDisable(selected);
        this.fiveMinButton.setDisable(selected);
        this.tenMinButton.setDisable(selected);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onStartClicked(){
        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start");
        alert.setHeaderText("Starting Test for " + this.studySet.getTitle());
        alert.setContentText("Are you sure you want to start?");
        Optional<ButtonType> alertResult = alert.showAndWait();
        if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
            TestData testData = new TestData(
                this.testType, 
                this.studySet, 
                Integer.parseInt(this.totalQuestions.getText()), 
                this.timeToggleButton.isSelected(), 
                this.timeToggleButton.isSelected() ? 
                Converter.toTimeSetting(
                    ((ToggleButton)timeToggleGroup.getSelectedToggle()).getText()
                ) : null,
                this.shuffleToggleButton.isSelected(),
                this.continuousToggleButton.isSelected()
            );
            this.testNavigator.show(TestPageURL.PLAY, testData);
        }
    }
}