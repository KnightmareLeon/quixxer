package io.github.knightmareleon.features.sets.components;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.navigator.BaseTabNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class SetsNavigator extends BaseTabNavigator {

    public SetsNavigator(Pane container, AppContext context) {
        super(container, context);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void show(String tabId) {
        try {
            String fxmlPath = this.getFXMLPath(tabId);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            loader.setControllerFactory(type -> {
                try {
                    return type.getConstructor(SetsService.class)
                            .newInstance(this.getContext().getSetService());
                } catch (NoSuchMethodException e) {
                    try {
                        return type.getDeclaredConstructor().newInstance();
                    } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });

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
