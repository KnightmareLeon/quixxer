package io.github.knightmareleon.features.test.components.pages.setup;

import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.TestConfig;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class TestMultipleChoiceSetupController extends TestSetupController{

    @FXML private ToggleButton randomized;

    @Override
    protected void onBackPageClicked() {
        this.testNavigator.show(TestPageURL.SETS, TestType.MULTIPLE_CHOICE);
    }

    @Override
    protected void onStartClicked() {
        TestConfig.Builder configBuilder = createGeneralTestConfig(TestType.MULTIPLE_CHOICE);
        if(configBuilder == null) return;
        configBuilder.setRandomized(randomized.isSelected());
        this.testNavigator.show(TestPageURL.PLAY, configBuilder.build());
    }
    
}
