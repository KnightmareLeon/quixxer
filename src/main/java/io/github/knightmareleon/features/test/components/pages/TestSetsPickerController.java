package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.TestType;
import io.github.knightmareleon.features.test.components.TestNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestSetsPickerController implements TestPage{

    private TestNavigator navigator;
    private TestType type;

    @FXML private Label testLabel;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.navigator = testNavigator;
    }

    public void setTestType(TestType type){
        this.type = type;
        switch(this.type){
            case TestType.MULTIPLE_CHOICE -> testLabel.setText("Multiple Choice");
            case TestType.FLASHCARD -> testLabel.setText("Flashcard");
            case TestType.MATCHING_TYPE -> testLabel.setText("Matching Type");
            case TestType.ENUMERATION -> testLabel.setText("Enumeration");
            case TestType.TRUE_OR_FALSE -> testLabel.setText("True or False");
            case TestType.COMBINED -> testLabel.setText("Combined");
            default -> testLabel.setText("Unknown Type");
        }
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
