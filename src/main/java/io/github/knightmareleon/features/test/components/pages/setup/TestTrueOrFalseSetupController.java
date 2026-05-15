package io.github.knightmareleon.features.test.components.pages.setup;

import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.TestConfig;

public class TestTrueOrFalseSetupController extends TestSetupController{

    @Override
    protected void onBackPageClicked() {
        this.testNavigator.show(TestPageURL.SETS, TestType.TRUE_OR_FALSE);
    }

    @Override
    protected void onStartClicked() {
        TestConfig.Builder configBuilder = createGeneralTestConfig(TestType.TRUE_OR_FALSE);
        if(configBuilder == null) return;
        this.testNavigator.show(TestPageURL.PLAY, configBuilder.build());
    }
    
}
