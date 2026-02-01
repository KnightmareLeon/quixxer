package io.github.knightmareleon.shared.navigator;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class TabNavigator {
    
    private final StackPane container;

    public TabNavigator(StackPane container) {
        this.container = container;
    }

    public void show(String tabId) {
        try {
            String fxmlPath = switch (tabId) {
                case "dashboard" -> "/io/github/knightmareleon/features/dashboard/DashboardView.fxml";
                case "test" -> "/io/github/knightmareleon/features/test/TestView.fxml";
                case "sets" -> "/io/github/knightmareleon/features/sets/SetsView.fxml";
                case "settings" -> "/io/github/knightmareleon/features/settings/SettingsView.fxml";
                default -> throw new IllegalArgumentException("Unknown tab: " + tabId);
            };

            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            container.getChildren().setAll(view);

        } catch (IOException e) {

        }
    }
}
