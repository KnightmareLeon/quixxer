package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;

public class TestTrueOrFalseSetupController extends TestSetupController{

    @Override
    protected void onBackPageClicked() {
        this.testNavigator.show(TestPageURL.SETS, TestType.TRUE_OR_FALSE);
    }

    @Override
    protected void onStartClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
