package io.github.knightmareleon.shared.infrastructure.navigator;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.scene.layout.Pane;

public class RootTabNavigator extends BaseTabNavigator{

    public RootTabNavigator(Pane container, AppContext context) {
        super(container, context);
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
