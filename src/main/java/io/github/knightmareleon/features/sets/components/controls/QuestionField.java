package io.github.knightmareleon.features.sets.components.controls;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.constants.StandardStyleClass;
import io.github.knightmareleon.shared.ui.controls.IconButton;
import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class QuestionField extends VBox{

    @FXML private Label questionError;
    @FXML private TextArea question;
    @FXML private IconButton saveButton;
    @FXML private IconButton closeQuestionButton;
    @FXML private ComboBox<String> qTypePicker;

    private final ObservableList<String> qTypes = FXCollections.observableArrayList(
        "Identification",
        "Enumeration", 
        "True or False"
    );
    @FXML private VBox choices;
    private final List<HBox> choiceRows = new ArrayList<>();

    private final ToggleGroup trueOrFalse = new ToggleGroup();
    private final RadioButton trueButton = new RadioButton("True");
    private final RadioButton falseButton = new RadioButton("False");

    @FXML private Button addChoiceButton;

    @SuppressWarnings("LeakingThisInConstructor")
    public QuestionField() {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("QuestionField.fxml")
        );
        ControllerRootSetter.set(this, loader);
    }

    @FXML
    public void initialize(){
        this.addChoiceButton.setMaxWidth(Double.MAX_VALUE);
        this.qTypePicker.setItems(this.qTypes);

        this.trueButton.setToggleGroup(this.trueOrFalse);
        this.falseButton.setToggleGroup(this.trueOrFalse);
        this.trueButton.setSelected(true);

        this.trueButton.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
        this.falseButton.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

        this.qTypePicker.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.equals(oldVal)) {
                return;
            }

            choices.getChildren().clear();
            choiceRows.clear();

            if(newVal.equals("Identification") || newVal.equals("Enumeration")){
                addChoiceButton.setVisible(true);
                this.addChoice();
            } else {
                addChoiceButton.setVisible(false);

                choices.getChildren().addAll(this.trueButton, this.falseButton);
            }

        });

        this.qTypePicker.setValue("Identification");
    }

    @FXML
    private void addChoice(){
        if (choiceRows.size() == 5 && 
            this.qTypePicker.getValue().equals("Identification"))
            return;

        HBox row = new HBox();
        TextField choiceField = new TextField();
        IconButton deleteChoiceField = new IconButton(
            "dashicons-post-trash",
            StandardStyleClass.ICON_BASE_COLOR
        );

        row.setSpacing(6);
        row.setFillHeight(true);
        row.setAlignment(Pos.CENTER);

        choiceField.setMinHeight(24);
        choiceField.getStyleClass().addAll(
            StandardStyleClass.BORDER_RADIUS_15,
            StandardStyleClass.STANDARD_FONT);
        choiceField.setStyle("-fx-text-fill: black !important");
        HBox.setHgrow(choiceField, Priority.ALWAYS);

        deleteChoiceField.setMinHeight(32);
        deleteChoiceField.setMinWidth(32);
        deleteChoiceField.getStyleClass().add(
            StandardStyleClass.ICON_BUTTON_BASE_BG
        );

        if(qTypePicker.getValue().equals("Identification")){
            CheckBox checkBox = new CheckBox();

            row.getChildren().add(checkBox);

            if (choices.getChildren().isEmpty()){
                checkBox.setSelected(true);
            }
        }

        row.setSpacing(12);
        row.getChildren().addAll(choiceField, deleteChoiceField);
        choiceRows.add(row);

        deleteChoiceField.setOnAction(e -> {
            if (choiceRows.size() > 1){
                choices.getChildren().remove(row);
                choiceRows.remove(row);
            }
        });

        choices.getChildren().add(row);
    }

    public String getQuestion(){
        return this.question.getText();
    }

    public String getType(){
        return this.qTypePicker.getValue();
    }

    public List<String> getChoices(){
        List<String> choiceList = new ArrayList<>();
        if(this.getType().equals("True or False")){
            choiceList = List.of("True", "False");
            return choiceList;
        }
        int index = this.getType().equals("Identification") ? 1 : 0;
        for (HBox row : this.choiceRows) {
            choiceList.add(((TextField)row.getChildren().get(index)).getText());
        }

        return choiceList;
    }

    public List<Integer> getAnswers(){
        String type = this.getType();
        List<Integer> answerIndices = new ArrayList<>();
        switch(type){
            case "True or False" -> {
                return ((RadioButton) this.trueOrFalse.getSelectedToggle()).equals(this.trueButton) ?
                        List.of(0) : List.of(1) ;
            }
            case "Identification" -> {
                int i = 0;
                for (HBox row : this.choiceRows) {
                    if(((CheckBox)row.getChildren().get(0)).isSelected()) {
                        answerIndices.add(i++);
                    }
                }
            }
            case "Enumeration" -> {
                for(int j = 0; j < this.choiceRows.size(); j++){
                    answerIndices.add(j);
                }
            }
            default -> {
            }

        }
        return answerIndices;
    }

    public void setCloseButtonAction(EventHandler<ActionEvent> closeAction){
        this.closeQuestionButton.setOnAction(closeAction);
    }

    public void setErrorVisible(boolean visible){
        this.questionError.setVisible(visible);
        if(visible && !this.getStyleClass().contains("error-border")){
            this.getStyleClass().add("error-border");
        } else {
            this.getStyleClass().remove("error-border");
        }
    }

    public void setClosable(boolean closable){
        this.closeQuestionButton.setVisible(closable);
        this.closeQuestionButton.setDisable(closable);
    }

    public void lockQuestionType(QuestionType questionType){
        if(questionType == null){
            this.qTypePicker.setDisable(false);
            return;
        }
        this.qTypePicker.setValue(questionType.getName());
        this.qTypePicker.setDisable(true);
    }

    public void setSaveVisible(boolean value){
        this.saveButton.setVisible(value);
    }

    public void setSaveButtonAction(EventHandler<ActionEvent> eh){
        this.saveButton.setOnAction(eh);
    }
}