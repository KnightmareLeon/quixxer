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

    private List<StudySet> studySets;
    private final List<SetCardForm> studySetCards = new ArrayList<>();
    private final List<SetListForm> studySetList = new ArrayList<>();

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
        this.studySets = this.setsService.getStudySets(1).getValue();
        for(StudySet studySet : this.studySets){
            studySetCards.add(new SetCardForm(
                studySet.getimgpath().equals("default") ? 
                "dashicons-book-alt" : studySet.getimgpath(), 
                studySet.getTitle(), 
                studySet.getSubject(),
                studySet.getQuestions().size()
            ));
            studySetList.add(new SetListForm(                
                studySet.getimgpath().equals("default") ? 
                "dashicons-book-alt" : studySet.getimgpath(), 
                studySet.getTitle(), 
                studySet.getSubject(),
                studySet.getQuestions().size()
            ));
        }

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
            if (oldVal == null && !this.setsContainer.getChildren().isEmpty()) {
                return;
            }
            this.setsContainer.getChildren().clear();
            if((IconToggleButton) newVal == this.cardViewButton){
                this.setsList.getChildren().clear();
                for(int i = 0; i < studySetCards.size(); i++){
                    if(i % 2 != 0){
                        this.setsLeftCol.getChildren().add(studySetCards.get(i));
                    } else {
                        this.setsRightCol.getChildren().add(studySetCards.get(i));
                    }
                    
                }
                this.setsContainer.getChildren().addAll(this.setsRightCol, this.setsLeftCol);
            } else {
                this.setsLeftCol.getChildren().clear();
                this.setsRightCol.getChildren().clear();
                this.setsList.getChildren().addAll(this.studySetList);
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