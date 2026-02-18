package io.github.knightmareleon.shared.navigator;

import javafx.scene.layout.Pane;

public class RootTabNavigator extends BaseTabNavigator{

    public RootTabNavigator(Pane container) {
        super(container);
    }

    @Override
    public String getFXMLPath(String tabId) {
        return switch (tabId) {
            case "dashboard" -> "/io/github/knightmareleon/features/dashboard/DashboardView.fxml";
            case "test" -> "/io/github/knightmareleon/features/test/TestView.fxml";
            case "sets" -> "/io/github/knightmareleon/features/sets/SetsView.fxml";
            case "settings" -> "/io/github/knightmareleon/features/settings/SettingsView.fxml";
            default -> throw new IllegalArgumentException("Unknown tab: " + tabId);
        };
    }
    
}
