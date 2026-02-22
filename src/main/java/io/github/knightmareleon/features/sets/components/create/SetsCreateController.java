package io.github.knightmareleon.features.sets.components.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.QuestionField;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final ObservableList<String> subjects = FXCollections.observableArrayList(
        "Computer Science",
        "Physics",
        "Biology",
        "Chemistry",
        "Geology",
        "History",
        "Language"
    );

    @FXML private VBox questionContainer;
    private final List<QuestionField> questionFields = new ArrayList<>();

    public SetsCreateController(AppContext context){
        this.setsService = context.getStudySetService();
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    public void initialize(){
        this.subjectPicker.setItems(this.subjects);
        this.addQuestion();
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show("main");
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
            String qType = questionField.getType();
            List<Integer> answers = questionField.getAnswers();
            List<String> trueOrFalse = List.of("True","False");
            if(questionField.getType().equals("True or False")){
                questions.add(new Question(
                    0,
                    question,
                    qType,
                    trueOrFalse,
                    answers
                ));
            } else {
                questions.add(new Question(
                    0,
                    question,
                    qType,
                    questionField.getChoices(),
                    answers
                ));
            }
        }

        StudySet studySet = new StudySet(
            this.setTitle.getText(),
            this.subjectPicker.getValue(),
            "default",
            questions
        );

        
        Result<StudySet> result = setsService.saveStudySet(studySet);
        
        if(result.isSuccess()){
            titleErrorLabel.setVisible(false);
            subjectErrorLabel.setVisible(false);
            for(QuestionField questionField: this.questionFields){
                questionField.setErrorVisible(false);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Result");
            alert.setHeaderText("Study Set Saved");
            alert.setContentText(String.format(
                "Study set %s under subject %s has been saved successfully!",
                result.getValue().getTitle(),
                result.getValue().getSubject()));
            Optional<ButtonType> alertResult = alert.showAndWait();
            if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
                navigator.show("main");
            }
            System.out.println(result.getValue());
        } else {

            boolean missingFields = false;

            int index = 0;

            boolean titleError = errorExists(result.getErrorMessages(), "Title Missing");
            titleErrorLabel.setVisible(titleError);
            if(titleError) {index++; missingFields = true;}

            boolean subjectError = errorExists(result.getErrorMessages(), "Subject Missing");
            subjectErrorLabel.setVisible(subjectError);
            if(subjectError){index++; missingFields = true;}

            int qIndex = 0;

            for(int i = index; i < result.getErrorMessages().size(); i++){
                int qErrorIndex = Integer.parseInt(result.getErrorMessages().get(i));
                while(qIndex < qErrorIndex){
                    this.questionFields.get(qIndex++).setErrorVisible(false);
                }
                qIndex = qErrorIndex + 1;
                this.questionFields.get(qErrorIndex).setErrorVisible(true);
                missingFields = true;
                }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            String headerText = "";
            String contentText = "";

            if(missingFields){
                headerText += "Missing required fields.";
                contentText += "Please fill in all required fields.";
            }

            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.showAndWait();
        }
    }

    private boolean errorExists(List<String> errors, String errorTarget){
        int limit = errors.size() > 3 ? 3 : errors.size();

        for(int i = 0; i < limit; i++){
            if(errors.get(i).equals(errorTarget)){
                return true;
            }
        }
        return false;
    }

}