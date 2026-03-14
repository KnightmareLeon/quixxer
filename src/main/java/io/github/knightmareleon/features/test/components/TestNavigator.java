package io.github.knightmareleon.features.test.components;

import java.io.IOException;

import io.github.knightmareleon.features.test.TestService;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.features.test.components.pages.TestPage;
import io.github.knightmareleon.shared.constants.PageURL;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.navigator.BaseTabNavigator;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.models.TestData;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class TestNavigator extends BaseTabNavigator {

    public TestNavigator(Pane container, AppContext context) {
        super(container, context);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void show(PageURL pageURL, Object... objects) {
        try {
            FXMLLoader loader = this.getLoader(pageURL);

            Parent view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof TestPage page) {
                page.setTestNavigator(this);
            }
            for (Object object : objects) {
                if(controller instanceof TestTypeReceiver receiver && 
                    object instanceof TestType testType){
                    receiver.receiveTestType(testType);
                }
                if(controller instanceof StudySetReceiver receiver && 
                    object instanceof StudySet studySet){
                    receiver.receiveStudySet(studySet);
                }
                if(controller instanceof TestDataReceiver receiver && 
                    object instanceof TestData testData){
                    receiver.receiveTestData(testData);
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
