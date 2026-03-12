package io.github.knightmareleon.shared.infrastructure.navigator;

import java.io.IOException;

import io.github.knightmareleon.shared.constants.PageURL;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    public void show(PageURL pageURL, Object... objects) {
        try {
            FXMLLoader loader = this.getLoader(pageURL);

            Parent view = loader.load();
            
            this.container.getChildren().setAll(view);

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

    protected FXMLLoader getLoader(PageURL pageURL){
        String fxmlPath = this.getFXMLPath(pageURL);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        loader.setControllerFactory(this::navigatorControllerFactory);

        return loader;
    }

    protected <T> T navigatorControllerFactory(Class<T> type) {
        try {
            return type
                    .getConstructor(AppContext.class)
                    .newInstance(this.getContext());
        } catch (NoSuchMethodException e) {
            try {
                return type
                        .getDeclaredConstructor()
                        .newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException("Failed to create controller: " + type.getName(), ex);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create controller: " + type.getName(), e);
        }
    }

    protected void setTransition(Node node){
        Transitions.standardFadeTransition(node);
    }

    protected String getFXMLPath(PageURL pageURL){
        return pageURL.getURL();
    }
}
