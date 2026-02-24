package io.github.knightmareleon.features.sets.components.main;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.SetCardForm;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class SetsMainController implements SetsPage{

    private SetsNavigator navigator;
    private final SetsService setsService;

    @FXML private IconToggleButton cardViewButton;
    @FXML private IconToggleButton listViewButton;
    @FXML private VBox setsList;

    public SetsMainController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    public void initialize(){
        ToggleGroup viewToggleGroup = new ToggleGroup();
        
        this.cardViewButton.setToggleGroup(viewToggleGroup);
        this.listViewButton.setToggleGroup(viewToggleGroup);

        this.cardViewButton.setSelected(true);

        viewToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) oldVal.setSelected(true);
        });

        this.setsList.getChildren().add(new SetCardForm("dashicons-book-alt", "Sample", "Subject", 100));
    }

    @FXML
    @SuppressWarnings("unused")
    private void onCreateSetsClicked() {
        navigator.show("create");
    }

}
