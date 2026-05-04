package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.TestConfig;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class TestFlashcardSetupController extends TestSetupController{

    @FXML private ToggleButton correctMarkButton;
    @FXML private ToggleButton textInputButton;

    @FXML private ToggleButton casesButton;
    @FXML private ToggleButton punctuationButton;
    @FXML private ToggleButton spacesButton;

    private final ToggleGroup inputStyleGroup = new ToggleGroup();

    @FXML
    public void initialize(){
        this.correctMarkButton.setToggleGroup(this.inputStyleGroup);
        this.textInputButton.setToggleGroup(this.inputStyleGroup);
        
        this.inputStyleGroup.selectedToggleProperty().addListener(
                (obsVal, oldVal, newVal) -> {
                    if (newVal == null) oldVal.setSelected(true);
                }
            );

        this.textInputButton.selectedProperty().addListener(
            (obs, oldVal, newVal) -> {
                casesButton.setDisable(oldVal);
                punctuationButton.setDisable(oldVal);
                spacesButton.setDisable(oldVal);
            }
        );

        this.correctMarkButton.setSelected(true);

        this.casesButton.setDisable(!this.textInputButton.isSelected());
        this.punctuationButton.setDisable(!this.textInputButton.isSelected());
        this.spacesButton.setDisable(!this.textInputButton.isSelected());
    }

    @Override
    protected void onBackPageClicked() {
        this.testNavigator.show(TestPageURL.SETS, TestType.FLASHCARD);
    }

    @Override
    protected void onStartClicked() {
        TestConfig.Builder configBuilder = createGeneralTestConfig(TestType.FLASHCARD);
        boolean inTextInput = this.textInputButton.isSelected();
        if(inTextInput){
            configBuilder.setIfTextInput(inTextInput);
            configBuilder.setIgnoreCases(this.casesButton.isSelected());
            configBuilder.setIgnorePunctuation(this.punctuationButton.isSelected());
            configBuilder.setIgnoreSpaces(this.spacesButton.isSelected());
        }
        this.testNavigator.show(TestPageURL.PLAY, configBuilder.build());
    }
}
