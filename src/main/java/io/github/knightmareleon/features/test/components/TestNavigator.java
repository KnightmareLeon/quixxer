package io.github.knightmareleon.features.test.components;

import java.io.IOException;

import io.github.knightmareleon.features.test.TestService;
import io.github.knightmareleon.features.test.components.pages.TestPage;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.navigator.BaseTabNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class TestNavigator extends BaseTabNavigator {

    public TestNavigator(Pane container, AppContext context) {
        super(container, context);
    }

        @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void show(String tabId, Object... objects) {
        try {
            FXMLLoader loader = this.getLoader(tabId);

            Parent view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof TestPage page) {
                page.setTestNavigator(this);
            }
            this.getContainer().getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFXMLPath(String tabId) {
        return switch (tabId) {
            case "main" -> "/io/github/knightmareleon/features/test/components/pages/TestMainView.fxml";
            case "sets" -> "/io/github/knightmareleon/features/test/components/pages/TestSetsPickerView.fxml";
            default -> throw new IllegalArgumentException("Unknown tab: " + tabId);
        };
    }

    @Override
    protected <T> T navigatorControllerFactory(Class<T> type) {
        try {
            return type
                    .getConstructor(TestService.class)
                    .newInstance(this.getContext().getTestService());
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
    
}
