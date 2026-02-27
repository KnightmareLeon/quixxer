package io.github.knightmareleon.features.sets.components;

import java.io.IOException;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.pages.SetDetailsController;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.navigator.BaseTabNavigator;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class SetsNavigator extends BaseTabNavigator {

    public SetsNavigator(Pane container, AppContext context) {
        super(container, context);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void show(String tabId, Object... objects) {
        try {
            FXMLLoader loader = this.getLoader(tabId);

            Parent view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof SetsPage page) {
                page.setSetsNavigator(this);
            }
            if (controller instanceof SetDetailsController setDetailsController && 
                objects.length > 0 && objects[0] instanceof StudySet studySet
            ){
                setDetailsController.setStudySet(studySet);
            }
            this.getContainer().getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFXMLPath(String tabId) {
        return switch (tabId) {
            case "main" -> "/io/github/knightmareleon/features/sets/components/pages/SetsMainView.fxml";
            case "create" -> "/io/github/knightmareleon/features/sets/components/pages/SetsCreateView.fxml";
            case "details" -> "/io/github/knightmareleon/features/sets/components/pages/SetDetailsView.fxml";
            default -> throw new IllegalArgumentException("Unknown tab: " + tabId);
        };
    }

    @Override
    protected <T> T navigatorControllerFactory(Class<T> type) {
        try {
            return type
                    .getConstructor(SetsService.class)
                    .newInstance(this.getContext().getSetService());
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
