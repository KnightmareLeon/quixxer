package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;

public class TestEnumerationSetupController extends TestSetupController{

    @Override
    protected void onBackPageClicked() {
        this.testNavigator.show(TestPageURL.SETS, TestType.ENUMERATION);
    }

    @Override
    protected void onStartClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}