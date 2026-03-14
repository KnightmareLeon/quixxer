package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestDataReceiver;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.shared.models.TestData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TestPlayerController implements TestPage, TestDataReceiver{

    private TestNavigator testNavigator;
    private TestData testData;

    @FXML private Label testPlayerTitle;
    
    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveTestData(TestData testData) {
        this.testData = testData;
        this.testPlayerTitle.setText(this.testData.getStudySetTitle() + " Test");
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.testNavigator.show(TestPageURL.SETS, this.testData.getType());
    }
}