package io.github.knightmareleon.shared.infrastructure.navigator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public abstract class BaseTabNavigator {
    
    private final Pane container;
    private final AppContext context;

    public BaseTabNavigator(Pane container, AppContext context) {
        this.container = container;
        this.context = context;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void show(String tabId) {
        try {
            String fxmlPath = this.getFXMLPath(tabId);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            
            loader.setControllerFactory(type -> {
                try {
                    return type.getConstructor(AppContext.class)
                            .newInstance(this.context);
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
            container.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Pane getContainer(){
        return this.container;
    }

    protected AppContext getContext(){
        return this.context;
    }

    public abstract String getFXMLPath(String tabId);
}
