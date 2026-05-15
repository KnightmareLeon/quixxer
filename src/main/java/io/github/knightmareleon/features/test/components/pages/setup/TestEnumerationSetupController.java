package io.github.knightmareleon.features.test.components.pages.setup;

import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.TestConfig;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class TestEnumerationSetupController extends TestSetupController{

    @FXML private ToggleButton casesButton;
    @FXML private ToggleButton punctuationButton;
    @FXML private ToggleButton spacesButton;
    @FXML private ToggleButton orderButton;

    @Override
    protected void onBackPageClicked() {
        this.testNavigator.show(TestPageURL.SETS, TestType.ENUMERATION);
    }

    @Override
    protected void onStartClicked() {
        TestConfig.Builder configBuilder = createGeneralTestConfig(TestType.ENUMERATION);
        if(configBuilder == null) return;
        configBuilder.setIgnoreCases(this.casesButton.isSelected());
        configBuilder.setIgnorePunctuation(this.punctuationButton.isSelected());
        configBuilder.setIgnoreSpaces(this.spacesButton.isSelected());
        configBuilder.setRandomized(!orderButton.isSelected());
        this.testNavigator.show(TestPageURL.PLAY, configBuilder.build());

    }
    
}