package io.github.knightmareleon.features.sets.components.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.shared.ui.controls.IconButton;
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
    @FXML private IconButton closeQuestionButton;
    @FXML private ComboBox<String> qTypePicker;
    private ObservableList<String> qTypes = FXCollections.observableArrayList(
        "Identification",
        "Enumeration", 
        "True or False"
    );
    @FXML private VBox choices;
    @FXML private List<HBox> choiceRows = new ArrayList<>();

    private ToggleGroup trueOrFalse = new ToggleGroup();
    private RadioButton trueButton = new RadioButton("True");
    private RadioButton falseButton = new RadioButton("False");

    @FXML private Button addChoiceButton = new Button();

    @SuppressWarnings("LeakingThisInConstructor")
    public QuestionField() {

        FXMLLoader loader;
        loader = new FXMLLoader(
                getClass().getResource("QuestionField.fxml")
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
        this.addChoiceButton.setMaxWidth(Double.MAX_VALUE);
        this.qTypePicker.setItems(this.qTypes);

        this.trueButton.setToggleGroup(trueOrFalse);
        this.falseButton.setToggleGroup(trueOrFalse);
        this.trueButton.setSelected(true);

        this.trueButton.getStyleClass().add("standard-font");
        this.falseButton.getStyleClass().add("standard-font");

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

                choices.getChildren().addAll(trueButton, falseButton);
            }

        });

        this.qTypePicker.setValue("Identification");
    }

    @FXML
    private void addChoice(){
        HBox row = new HBox();
        TextField choiceField = new TextField();
        IconButton deleteChoiceField = new IconButton(
            "dashicons-post-trash",
            "icon-base-color"
        );

        row.setSpacing(6);
        row.setFillHeight(true);
        row.setAlignment(Pos.CENTER);

        choiceField.setMinHeight(24);
        choiceField.getStyleClass().addAll("border-radius-15","standard-font");
        choiceField.setStyle("-fx-text-fill: black !important");
        HBox.setHgrow(choiceField, Priority.ALWAYS);

        deleteChoiceField.setMinHeight(32);
        deleteChoiceField.setMinWidth(32);
        deleteChoiceField.getStyleClass().add("icon-button-base-bg");

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
            return choiceList;
        }
        int index = this.getType().equals("Identification") ? 1 : 0;
        for (HBox row : this.choiceRows) {
            choiceList.add(((TextField)row.getChildren().get(index)).getText());
        }

        return choiceList;
    }

    public List<Integer> getAnswers(){
        if(this.getType().equals("True or False")){
            return ((RadioButton) this.trueOrFalse.getSelectedToggle()).equals(trueButton) ?
                List.of(0) : List.of(1) ;
        } else if (this.getType().equals("Identification")){
            List<Integer> answerIndices = new ArrayList<>();
            int i = 0;
            for (HBox row : this.choiceRows) {
                if(((CheckBox)row.getChildren().get(0)).isSelected()) {
                    answerIndices.add(i++);
                }
            }

            return answerIndices;
        }
        return new ArrayList<>();
    }

    public void setCloseButtonAction(EventHandler<ActionEvent> closeAction){
        this.closeQuestionButton.setOnAction(closeAction);
    }

    public void setErrorVisible(boolean visible){
        this.questionError.setVisible(visible);
        if(visible){
            this.getStyleClass().add("error-border");
        } else {
            this.getStyleClass().remove("error-border");
        }
    }
}