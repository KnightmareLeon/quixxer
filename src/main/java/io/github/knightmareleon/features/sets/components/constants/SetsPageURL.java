package io.github.knightmareleon.features.sets.components.constants;

import io.github.knightmareleon.shared.constants.PageURL;

public enum SetsPageURL implements PageURL{
    MAIN("/io/github/knightmareleon/features/sets/components/pages/SetsMainView.fxml"),
    CREATE("/io/github/knightmareleon/features/sets/components/pages/SetsCreateView.fxml"),
    DETAILS("/io/github/knightmareleon/features/sets/components/pages/SetDetailsView.fxml");
    ;

    private final String url;

    private SetsPageURL(String url) {
        this.url = url;
    }

    @Override
    public String getURL() {return this.url;}
    
}
