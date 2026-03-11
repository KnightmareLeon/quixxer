package io.github.knightmareleon.shared.constants;

public enum MainPageURL implements PageURL{
    DASHBOARD("/io/github/knightmareleon/features/dashboard/DashboardView.fxml"),
    TEST("/io/github/knightmareleon/features/test/TestView.fxml"),
    SETS("/io/github/knightmareleon/features/sets/SetsView.fxml"),
    SETTINGS("/io/github/knightmareleon/features/settings/SettingsView.fxml");
    
    private final String url;

    private MainPageURL(String url) {
        this.url = url;
    }

    @Override
    public String getURL() {return this.url;}
}
