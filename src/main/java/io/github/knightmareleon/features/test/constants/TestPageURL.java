package io.github.knightmareleon.features.test.constants;

import io.github.knightmareleon.shared.constants.PageURL;

public enum TestPageURL implements PageURL{
    MAIN("/io/github/knightmareleon/features/test/components/pages/TestMainView.fxml"),
    SETS("/io/github/knightmareleon/features/test/components/pages/TestSetsPickerView.fxml"),
    SETUP("/io/github/knightmareleon/features/test/components/pages/setup/TestSetupView.fxml"),
    SETUP_MULTI("/io/github/knightmareleon/features/test/components/pages/setup/TestMultipleChoiceSetupView.fxml"),
    SETUP_FCARD("/io/github/knightmareleon/features/test/components/pages/setup/TestFlashcardSetupView.fxml"),
    SETUP_ENUME("/io/github/knightmareleon/features/test/components/pages/setup/TestEnumerationSetupView.fxml"),
    SETUP_TROFS("/io/github/knightmareleon/features/test/components/pages/setup/TestTrueOrFalseSetupView.fxml"),
    PLAY("/io/github/knightmareleon/features/test/components/pages/TestPlayerView.fxml");

    private final String url;

    TestPageURL(String url) {
        this.url = url;
    }

    @Override
    public String getURL(){return this.url;}

}
