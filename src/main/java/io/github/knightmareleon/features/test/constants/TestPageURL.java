package io.github.knightmareleon.features.test.constants;

import io.github.knightmareleon.shared.constants.PageURL;

public enum TestPageURL implements PageURL{
    MAIN("/io/github/knightmareleon/features/test/components/pages/TestMainView.fxml"),
    SETS("/io/github/knightmareleon/features/test/components/pages/TestSetsPickerView.fxml"),
    SETUP("/io/github/knightmareleon/features/test/components/pages/TestSetupView.fxml"),
    PLAY("/io/github/knightmareleon/features/test/components/pages/TestPlayerView.fxml");
    private final String url;

    TestPageURL(String url) {
        this.url = url;
    }

    @Override
    public String getURL(){return this.url;}

}
