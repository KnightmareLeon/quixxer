package io.github.knightmareleon.features.sets.components.create;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.QuestionField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class SetsCreateController implements SetsPage{

    private SetsNavigator navigator;

    @FXML
    private ComboBox<String> subjectPicker;
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
    private List<QuestionField> questionFields = new ArrayList<>();

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
}
