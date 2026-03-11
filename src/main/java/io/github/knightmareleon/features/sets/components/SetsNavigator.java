package io.github.knightmareleon.features.sets.components;

import java.io.IOException;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.pages.SetsPage;
import io.github.knightmareleon.shared.constants.PageURL;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.navigator.BaseTabNavigator;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class SetsNavigator extends BaseTabNavigator {

    public SetsNavigator(Pane container, AppContext context) {
        super(container, context);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void show(PageURL pageURL, Object... objects) {
        try {
            FXMLLoader loader = this.getLoader(pageURL);

            Parent view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof SetsPage page) {
                page.setSetsNavigator(this);
            }

            for(Object object: objects){
                if (controller instanceof StudySetReceiver receiver && object instanceof StudySet studySet){
                    receiver.receiveStudySet(studySet);
                }
            }

            this.getContainer().getChildren().setAll(view);
            this.setTransition(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
