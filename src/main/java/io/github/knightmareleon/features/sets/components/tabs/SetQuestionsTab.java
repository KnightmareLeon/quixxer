package io.github.knightmareleon.features.sets.components.tabs;

import io.github.knightmareleon.features.sets.components.controls.EnumerationQuestionDetail;
import io.github.knightmareleon.features.sets.components.controls.IdentificationQuestionDetail;
import io.github.knightmareleon.features.sets.components.controls.TrueOrFalseQuestionDetail;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class SetQuestionsTab extends TabPane{
    
    private final StudySet studySet;

    @FXML private VBox identTab;
    @FXML private VBox enumTab;
    @FXML private VBox tofTab;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetQuestionsTab(StudySet studySet) {

        this.studySet = studySet;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("SetQuestionsTab.fxml")
        );
        ControllerRootSetter.set(this, loader);

        this.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Transitions.standardFadeTransition(newTab.getContent(), 1);
        });

    }

    @FXML
    public void initialize(){
        for(Question question: this.studySet.getQuestions()){
            switch(question.getType()){
                case QuestionType.IDENTIFICATION -> 
                    identTab.getChildren().add(
                        new IdentificationQuestionDetail(question)
                    );
                case QuestionType.ENUMERATION ->
                    enumTab.getChildren().add(
                        new EnumerationQuestionDetail(question)
                    );
                default ->
                    tofTab.getChildren().add(
                        new TrueOrFalseQuestionDetail(question)
                    );
            }
        }

        final Button addNewIdentQuestion = new Button("Add New Idenitification Question");
        final Button addNewEnumQuestion = new Button("Add New Enumeration Question");
        final Button addNewTOFQuestion = new Button("Add New True or False Question");

        
    }
}
