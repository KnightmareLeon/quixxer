package io.github.knightmareleon.features.sets.components.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.shared.ui.controls.IconButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class QuestionField extends VBox{

    @FXML private TextArea question;
    @FXML private ComboBox<String> qTypePicker;
    private ObservableList<String> qTypes = FXCollections.observableArrayList(
        "Identification",
        "Enumeration", 
        "True or False"
    );
    @FXML private VBox choices;
    @FXML private List<HBox> choiceRows = new ArrayList<>();
    @FXML private Button addChoiceButton = new Button();

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
        this.qTypePicker.setItems(this.qTypes);
        this.addChoice();
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

        choiceField.setMinHeight(48);
        choiceField.getStyleClass().add("border-radius-15");

        HBox.setHgrow(choiceField, Priority.ALWAYS);
        deleteChoiceField.getStyleClass().add("icon-button-base-bg");
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


}
