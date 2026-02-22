package io.github.knightmareleon.features.sets.components.create;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.QuestionField;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SetsCreateController implements SetsPage{

    private final SetsCreateService service = new SetsCreateService();
    private SetsNavigator navigator;

    @FXML private TextField setTitle;
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

        service.saveStudySet(studySet);
    }
}
