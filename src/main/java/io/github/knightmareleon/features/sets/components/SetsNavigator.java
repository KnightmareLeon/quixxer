package io.github.knightmareleon.features.sets.components;

import java.io.IOException;

import io.github.knightmareleon.shared.navigator.BaseTabNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class SetsNavigator extends BaseTabNavigator {

    public SetsNavigator(Pane container) {
        super(container);
    }

    @Override
    public void show(String tabId) {
        try {
            String fxmlPath = this.getFXMLPath(tabId);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            Object controller = loader.getController();
            if (controller instanceof SetsPage page) {
                page.setSetsNavigator(this);
            }
            this.getContainer().getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFXMLPath(String tabId) {
        return switch (tabId) {
            case "main" -> "/io/github/knightmareleon/features/sets/components/main/SetsMainView.fxml";
            case "create" -> "/io/github/knightmareleon/features/sets/components/create/SetsCreateView.fxml";
            default -> throw new IllegalArgumentException("Unknown tab: " + tabId);
        };
    }
    
}
