package io.github.knightmareleon.features.test.components.controls;

import io.github.knightmareleon.shared.constants.TimeSetting;
import io.github.knightmareleon.shared.ui.controls.NaturalNumberField;
import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import io.github.knightmareleon.shared.utils.Converter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class TestGeneralSettings extends VBox{

    @FXML private NaturalNumberField totalQuestionsField;
    @FXML private Label totalQuestionsMax;

    @FXML private VBox configurationContainer;

    @FXML private ToggleButton timeToggleButton;
    @FXML private ToggleButton thirtySecButton;
    @FXML private ToggleButton oneMinButton;
    @FXML private ToggleButton threeMinButton;
    @FXML private ToggleButton fiveMinButton;
    @FXML private ToggleButton tenMinButton;

    private final ToggleGroup timeToggleGroup = new ToggleGroup();
    @FXML private ToggleButton shuffleToggleButton;
    @FXML private ToggleButton continuousToggleButton;

    @SuppressWarnings("LeakingThisInConstructor")
    public TestGeneralSettings(){
        FXMLLoader loader = new FXMLLoader(
        getClass().getResource("TestGeneralSettings.fxml")
        );
        ControllerRootSetter.set(this, loader);
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
            if (newVal == null) oldVal.setSelected(true);
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

    public int totalQuestions(){
        return Integer.parseInt(this.totalQuestionsField.getText());
    }

    public boolean timeToggled() {
        return this.timeToggleButton.isSelected();
    }

    public TimeSetting timeSetting(){
        return this.timeToggleButton.isSelected() ? 
            Converter.toTimeSetting(
                ((ToggleButton)timeToggleGroup.getSelectedToggle()).getText()
            ) : null;
    }

    public boolean continuousToggled(){
        return this.continuousToggleButton.isSelected();
    }

    public boolean shuffleToggled(){
        return this.shuffleToggleButton.isSelected();
    }

    public void setMaxTotalQuestions(int maxTotalQuestions ){
        this.totalQuestionsField.setMaxNumber(maxTotalQuestions);
        this.totalQuestionsField.setTextToMax();
        this.totalQuestionsMax.setText("/" + maxTotalQuestions);
    }
}