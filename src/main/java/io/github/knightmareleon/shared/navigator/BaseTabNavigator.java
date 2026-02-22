package io.github.knightmareleon.shared.navigator;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public abstract class BaseTabNavigator {
    
    private final Pane container;

    public BaseTabNavigator(Pane container) {
        this.container = container;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void show(String tabId) {
        try {
            String fxmlPath = this.getFXMLPath(tabId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            container.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Pane getContainer(){
        return this.container;
    }

    public abstract String getFXMLPath(String tabId);
}
