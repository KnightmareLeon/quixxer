package io.github.knightmareleon.features.sets.components.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.controls.QuestionField;
import io.github.knightmareleon.features.sets.constants.SetsConstants;
import io.github.knightmareleon.features.sets.constants.SetsPageURL;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SetsCreateController implements SetsPage{

    private final SetsService setsService;
    private SetsNavigator navigator;

    @FXML private Label titleErrorLabel;
    @FXML private TextField setTitle;

    @FXML private Label subjectErrorLabel;
    @FXML private ComboBox<String> subjectPicker;

    @FXML private VBox questionContainer;
    private final List<QuestionField> questionFields = new ArrayList<>();

    public SetsCreateController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    public void initialize(){
        this.subjectPicker.setItems(SetsConstants.SUBJECTS);
        this.addQuestion();
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show(SetsPageURL.MAIN);
    }

    @FXML
    private void addQuestion(){
        QuestionField questionField = new QuestionField();

        questionField.setCloseButtonAction(e -> {
            if (questionContainer.getChildren().size() > 1){
                questionContainer.getChildren().remove(questionField);
                questionFields.remove(questionField);
            }
        });

        questionFields.add(questionField);
        questionContainer.getChildren().add(questionField);
    }

    @FXML
    @SuppressWarnings("unused")
    private void saveSet(){
        List<Question> questions = new ArrayList<>();
        
        for (QuestionField questionField : this.questionFields) {
            String question = questionField.getQuestion();
            QuestionType qType = switch(questionField.getType()) {
                case "True or False" -> QuestionType.TRUE_OR_FALSE;
                case "Enumeration"-> QuestionType.ENUMERATION;
                default -> QuestionType.IDENTIFICATION;
            };
            List<String> choiceStrings = questionField.getChoices();
            List<Integer> answers = questionField.getAnswers();
            List<Choice> choices = new ArrayList<>();
            int answerIndex = 0;
            for(int i = 0; i < choiceStrings.size(); i++){
                boolean isAnswer = i == answers.get(answerIndex);
                choices.add(
                    new Choice(choiceStrings.get(i), 
                    isAnswer
                    )
                );
                if (isAnswer && answerIndex < answers.size() - 1) answerIndex++;
            }
            questions.add(new Question(
                question,
                qType,
                choices
            ));

        }

        StudySet studySet = new StudySet(
            this.setTitle.getText(),
            this.subjectPicker.getValue(),
            "default", //reminder to implement imgpath picker later
            questions
        );

        
        Result<StudySet> result = setsService.saveStudySet(studySet);

        if(result.isSuccess()){

            for(QuestionField questionField: this.questionFields){
                questionField.setErrorVisible(false);
            }

            Alert alert = new StandardAlert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Result");
            alert.setHeaderText("Study Set Saved");
            alert.setContentText(String.format(
                "Study set %s under subject %s has been saved successfully!",
                result.getValue().getTitle(),
                result.getValue().getSubject()));
            Optional<ButtonType> alertResult = alert.showAndWait();
            if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
                navigator.show(SetsPageURL.MAIN);
            }
            return;
        } 

        Alert alert = new StandardAlert(Alert.AlertType.ERROR);
        alert.setTitle("Save Error");

        String headerText;
        String contentText;
                
        boolean missingTitle = false;
        boolean missingSubject = false;
        boolean missingQuestionFields = false;

        if (result.getErrorMessages().get(0).equals(SetsConstants.DATABASE_ERROR)){
            headerText = "Database Error.";
            contentText = "An error occured in the database.";
        } else {

            List<String> headerStrings = new ArrayList<>();
            List<String> contentStrings = new ArrayList<>();

            int errorIndex = 0;

            missingTitle = errorExists(result.getErrorMessages(), SetsConstants.MISSING_TITLE_ERROR);
            if(missingTitle) {
                errorIndex++;
                contentStrings.add("Please fill in the Set Title field.");
            }

            missingSubject = errorExists(result.getErrorMessages(), SetsConstants.MISSING_SUBJECT_ERROR);
            if(missingSubject) {
                errorIndex++;
                contentStrings.add("Please fill in the Subject field.");
            }

            if(errorExists(result.getErrorMessages(), SetsConstants.DUPLICATE_STUDY_SET_ERROR)){
                errorIndex++;
                headerStrings.add("Unique Study Set."); 
                contentStrings.add(String.format(
                    "Study Set %s with subject %s already exists"
                    , studySet.getTitle(), studySet.getSubject()
                ));
            }

            for(int i = 0; i < this.questionFields.size(); i++){
                if(errorIndex > result.getErrorMessages().size() - 1) {
                    this.questionFields.get(i).setErrorVisible(false);
                    continue;
                }
                boolean hasError = Integer.parseInt(result.getErrorMessages().get(errorIndex)) == i;
                if(hasError) {
                    errorIndex++;
                    contentStrings.add("Please fill in the required fields for Question #" + (i + 1));
                    missingQuestionFields = true;
                }
                this.questionFields.get(i).setErrorVisible(hasError);
            }

            if (missingTitle || missingSubject || missingQuestionFields){
                headerStrings.add("Missing required fields.");
            }

            headerText = String.join(",", headerStrings);
            contentText = String.join("\n", contentStrings);

        }

        titleErrorLabel.setVisible(missingTitle);
        subjectErrorLabel.setVisible(missingSubject);

        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();

    }

    private boolean errorExists(List<String> errors, String errorTarget){
        int limit = errors.size() > 3 ? 3 : errors.size();

        for(int i = 0; i < limit; i++){
            if(errors.get(i).equals(errorTarget)) return true;
        }
        return false;
    }

}