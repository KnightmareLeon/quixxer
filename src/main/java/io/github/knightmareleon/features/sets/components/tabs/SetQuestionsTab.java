package io.github.knightmareleon.features.sets.components.tabs;

import java.io.IOException;

import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;

public class SetQuestionsTab extends TabPane{
    
    private StudySet studySet;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetQuestionsTab(StudySet studySet) {

        this.studySet = studySet;

        FXMLLoader loader;
        loader = new FXMLLoader(
                getClass().getResource("SetQuestionsTab.fxml")
        );
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
