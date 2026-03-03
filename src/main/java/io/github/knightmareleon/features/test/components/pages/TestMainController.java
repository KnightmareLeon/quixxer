package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestNavigator;

public class TestMainController implements TestPage{

    private TestNavigator testNavigator;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }
}
