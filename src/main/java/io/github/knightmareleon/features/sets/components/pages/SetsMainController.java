package io.github.knightmareleon.features.sets.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.SetCardForm;
import io.github.knightmareleon.features.sets.components.controls.SetListForm;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SetsMainController implements SetsPage{

    private SetsNavigator navigator;
    private final SetsService setsService;

    private final List<StudySet> studySets = new ArrayList<>();
    private final List<SetCardForm> studySetCards = new ArrayList<>();
    private final VBox setsLeftCol = new VBox(24);
    private final VBox setsRightCol = new VBox(24);
    private final VBox setsList = new VBox(24);

    @FXML private IconToggleButton cardViewButton;
    @FXML private IconToggleButton listViewButton;

    @FXML private HBox setsContainer;

    public SetsMainController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    public void initialize(){
        this.setsLeftCol.setFillWidth(true);
        this.setsRightCol.setFillWidth(true);
        HBox.setHgrow(this.setsLeftCol, Priority.ALWAYS);
        HBox.setHgrow(this.setsRightCol, Priority.ALWAYS);
        HBox.setHgrow(this.setsList, Priority.ALWAYS);

        ToggleGroup viewToggleGroup = new ToggleGroup();
        
        this.cardViewButton.setToggleGroup(viewToggleGroup);
        this.listViewButton.setToggleGroup(viewToggleGroup);

        viewToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
                return;
            }
            this.setsContainer.getChildren().clear();
            if((IconToggleButton) newVal == this.cardViewButton){
                this.setsList.getChildren().clear();
                SetCardForm set1 = new SetCardForm("dashicons-book-alt", "Sample", "Subject", 100);
                SetCardForm set2 = new SetCardForm("dashicons-book-alt", "Sample", "Subject", 100);
                SetCardForm set3 = new SetCardForm("dashicons-book-alt", "Sample", "Subject", 100);
                this.setsLeftCol.getChildren().addAll(set1,set2);
                this.setsRightCol.getChildren().add(set3);
                this.setsContainer.getChildren().addAll(this.setsLeftCol,this.setsRightCol);
            } else {
                this.setsLeftCol.getChildren().clear();
                this.setsRightCol.getChildren().clear();
                SetListForm set1 = new SetListForm("dashicons-book-alt", "Sample", "Subject", 100);
                SetListForm set2 = new SetListForm("dashicons-book-alt", "Sample", "Subject", 100);
                SetListForm set3 = new SetListForm("dashicons-book-alt", "Sample", "Subject", 100);
                this.setsList.getChildren().addAll(set1,set2,set3);
                this.setsContainer.getChildren().add(this.setsList);
            }

        });

        this.cardViewButton.setSelected(true);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onCreateSetsClicked() {
        navigator.show("create");
    }
}