package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import javafx.fxml.FXML;

public class TestMainController implements TestPage{

    private TestNavigator testNavigator;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }


    @FXML
    @SuppressWarnings("unused")
    private void onMultipleChoiceClicked(){
        this.onTestIconClicked(TestType.MULTIPLE_CHOICE);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onFlashcardClicked(){
        this.onTestIconClicked(TestType.FLASHCARD);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onMatchingTypeClicked(){
        this.onTestIconClicked(TestType.MATCHING_TYPE);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onEnumerationClicked(){
        this.onTestIconClicked(TestType.ENUMERATION);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onTrueOrFalseClicked(){
        this.onTestIconClicked(TestType.TRUE_OR_FALSE);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onCombinedClicked(){
        this.onTestIconClicked(TestType.COMBINED);
    }

    private void onTestIconClicked(TestType type){
        this.testNavigator.show(TestPageURL.SETS, type);
    }
}
