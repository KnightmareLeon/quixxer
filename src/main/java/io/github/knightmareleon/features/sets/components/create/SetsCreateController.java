package io.github.knightmareleon.features.sets.components.create;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SetsCreateController implements SetsPage{

    private SetsNavigator navigator;

    @FXML
    private ComboBox<String> subjectPicker;
    private ObservableList<String> subjects = FXCollections.observableArrayList(
        "Computer Science",
        "Physics",
        "Biology",
        "Chemistry",
        "Geology",
        "History",
        "Language"
    );

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    public void initialize(){
        this.subjectPicker.setItems(this.subjects);
    }

    @FXML
    private void onBackPageClicked() {
        navigator.show("main");
    }

}
