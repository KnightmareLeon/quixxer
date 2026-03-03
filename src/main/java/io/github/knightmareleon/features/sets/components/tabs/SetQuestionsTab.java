package io.github.knightmareleon.features.sets.components.tabs;

import java.io.IOException;

import io.github.knightmareleon.features.sets.components.controls.EnumerationQuestionDetail;
import io.github.knightmareleon.features.sets.components.controls.IdentificationQuestionDetail;
import io.github.knightmareleon.features.sets.components.controls.TrueOrFalseQuestionDetail;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class SetQuestionsTab extends TabPane{
    
    private StudySet studySet;

    @FXML private VBox identTab;
    @FXML private VBox enumTab;
    @FXML private VBox tofTab;

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

    @FXML
    public void initialize(){
        for(Question question: this.studySet.getQuestions()){
            switch(question.getType()){
                case QuestionType.IDENTIFICATION -> identTab.getChildren().add(
                        new IdentificationQuestionDetail(question)
                    );
                case QuestionType.ENUMERATION -> { enumTab.getChildren().add(
                        new EnumerationQuestionDetail(question)
                    );
                }
                default -> { tofTab.getChildren().add(
                        new TrueOrFalseQuestionDetail(question)
                    );
                }
            }
        }
    }
}
