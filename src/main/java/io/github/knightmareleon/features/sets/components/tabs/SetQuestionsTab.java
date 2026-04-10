package io.github.knightmareleon.features.sets.components.tabs;

import java.util.Optional;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.controls.EnumerationQuestionDetail;
import io.github.knightmareleon.features.sets.components.controls.IdentificationQuestionDetail;
import io.github.knightmareleon.features.sets.components.controls.QuestionField;
import io.github.knightmareleon.features.sets.components.controls.TrueOrFalseQuestionDetail;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import io.github.knightmareleon.shared.utils.Result;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class SetQuestionsTab extends TabPane{
    
    private final SetsService setsService;
    private final StudySet studySet;
    private final EventHandler<ActionEvent> onAddQuestionClicked;

    @FXML private VBox identContainer;
    @FXML private VBox enumContainer;
    @FXML private VBox tofContainer;

    @FXML private Button newIdnQstButton;
    @FXML private Button newEnmQstButton;
    @FXML private Button newTofQstButton;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public SetQuestionsTab(
        SetsService setsService,
        StudySet studySet,
        EventHandler<ActionEvent> onAddQuestionClicked
    ) 
    
    {
        this.setsService = setsService;
        this.studySet = studySet;
        this.onAddQuestionClicked = onAddQuestionClicked;

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
        int paddingAdder = -64;
        for(Question question: this.studySet.getQuestions()){
            switch(question.getType()){
                case QuestionType.IDENTIFICATION -> {
                    IdentificationQuestionDetail idntQstDetail = 
                        new IdentificationQuestionDetail(
                            question,
                            this.widthProperty().add(paddingAdder)
                        );
                    
                    idntQstDetail.setDeleteButtonAction(e -> {
                        if(this.studySet.getQuestions().size() <= 1) {
                            Alert alert = new StandardAlert(Alert.AlertType.ERROR);
                            alert.setTitle("Invalid Delete");
                            alert.setHeaderText("Cannot Delete Question");
                            alert.setContentText("Cannot delete the last remaining question of this study set.");
                            alert.showAndWait();
                            return;
                        }

                        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete");
                        alert.setHeaderText("Deleting Question");
                        alert.setContentText("Are you sure you want to delete this question?");
                        Optional<ButtonType> alertResult = alert.showAndWait();
                        if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
                            Result<String> result = this.setsService.deleteQuestionResult(
                                question.getId(), 
                                question.getType()
                            );

                            if(result.isSuccess()){
                                this.studySet.getQuestions().remove(question);
                                this.identContainer.getChildren().remove(idntQstDetail);
                            }
                        }
                    });

                    this.identContainer.getChildren().add(
                        idntQstDetail
                    );
                }
                case QuestionType.ENUMERATION -> {
                    EnumerationQuestionDetail enumQstDetail = new EnumerationQuestionDetail(
                        question, 
                        this.widthProperty().add(paddingAdder)
                    );

                    enumQstDetail.setDeleteButtonAction(e -> {
                        if(this.studySet.getQuestions().size() <= 1) {
                            Alert alert = new StandardAlert(Alert.AlertType.ERROR);
                            alert.setTitle("Invalid Delete");
                            alert.setHeaderText("Cannot Delete Question");
                            alert.setContentText("Cannot delete the last remaining question of this study set.");
                            alert.showAndWait();
                            return;
                        }

                        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete");
                        alert.setHeaderText("Deleting Question");
                        alert.setContentText("Are you sure you want to delete this question?");
                        Optional<ButtonType> alertResult = alert.showAndWait();
                        if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
                            Result<String> result = this.setsService.deleteQuestionResult(
                                question.getId(), 
                                question.getType()
                            );

                            if(result.isSuccess()){
                                this.studySet.getQuestions().remove(question);
                                this.enumContainer.getChildren().remove(enumQstDetail);
                            }
                        }

                    });

                    enumContainer.getChildren().add(
                        enumQstDetail
                    );
                }
                default -> {
                    TrueOrFalseQuestionDetail tofQstDetail = new TrueOrFalseQuestionDetail(
                        question,
                        this.widthProperty().add(paddingAdder)
                    );

                    tofQstDetail.setDeleteButtonAction(e -> {
                        if(this.studySet.getQuestions().size() <= 1) {
                            Alert alert = new StandardAlert(Alert.AlertType.ERROR);
                            alert.setTitle("Invalid Delete");
                            alert.setHeaderText("Cannot Delete Question");
                            alert.setContentText("Cannot delete the last remaining question of this study set.");
                            alert.showAndWait();
                            return;
                        }

                        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete");
                        alert.setHeaderText("Deleting Question");
                        alert.setContentText("Are you sure you want to delete this question?");
                        Optional<ButtonType> alertResult = alert.showAndWait();
                        
                        if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
                            Result<String> result = this.setsService.deleteQuestionResult(
                                question.getId(), 
                                question.getType()
                            );

                            if(result.isSuccess()){
                                this.studySet.getQuestions().remove(question);
                                this.tofContainer.getChildren().remove(tofQstDetail);
                            }
                        }
                    });
    
                    tofContainer.getChildren().add(
                        tofQstDetail
                    );
                }
            }
        }

        this.initNewQuestionButton(this.newIdnQstButton, this.identContainer, QuestionType.IDENTIFICATION);
        this.initNewQuestionButton(this.newEnmQstButton, this.enumContainer, QuestionType.ENUMERATION);
        this.initNewQuestionButton(this.newTofQstButton, this.tofContainer, QuestionType.TRUE_OR_FALSE);
    }

    public void initNewQuestionButton(Button button, VBox container, QuestionType questionType){
        button.setOnAction(e -> {
            if(!container.getChildren().isEmpty()){
                if(container.getChildren().getLast() instanceof QuestionField) return;
            }
            
            QuestionField newQuestion = new QuestionField();
            newQuestion.setCloseButtonAction(eh -> {
                container.getChildren().removeLast();
            });
            newQuestion.lockQuestionType(questionType);
            newQuestion.setSaveVisible(true);

            container.getChildren().add(newQuestion);
        });
    }

}