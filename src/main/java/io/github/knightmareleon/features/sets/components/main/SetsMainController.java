package io.github.knightmareleon.features.sets.components.main;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.SetCardForm;
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
    private VBox setsLeftCol = new VBox(24);
    private VBox setsRightCol = new VBox(24);
    private VBox setsList = new VBox(24);

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
                setsLeftCol.getChildren().addAll(set1,set2);
                setsRightCol.getChildren().add(set3);
                this.setsContainer.getChildren().addAll(this.setsLeftCol,this.setsRightCol);
            } else {
                setsLeftCol.getChildren().clear();
                setsRightCol.getChildren().clear();
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