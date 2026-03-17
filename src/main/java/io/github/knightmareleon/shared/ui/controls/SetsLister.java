package io.github.knightmareleon.shared.ui.controls;

import java.util.List;

import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SetsLister extends ScrollPane{

    private List<SetCardForm> studySetCards;
    private List<SetListForm> studySetList;

    private final VBox setsLeftCol = new VBox(24);
    private final VBox setsRightCol = new VBox(24);
    private final GridPane setsIconContainer = new GridPane(24, 24);
    private final ColumnConstraints setsLeftColCons = new ColumnConstraints();
    private final ColumnConstraints setsRightColCons = new ColumnConstraints();
    private final VBox setsList = new VBox(24);

    private IconToggleButton cardViewButton;
    private IconToggleButton listViewButton;

    @FXML private HBox setsContainer;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetsLister(){
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("SetsLister.fxml")
        );

        ControllerRootSetter.set(this, loader);
    }

    public void init(
            List<SetCardForm> studySetCards, 
            List<SetListForm> studySetList, 
            IconToggleButton cardViewButton,
            IconToggleButton listViewBUtton ){
        this.studySetCards = studySetCards;
        this.studySetList = studySetList;
        this.cardViewButton = cardViewButton;
        this.listViewButton = listViewBUtton;

        this.setsLeftCol.setFillWidth(true);
        this.setsRightCol.setFillWidth(true);
        this.setsLeftCol.setMaxWidth(Double.MAX_VALUE);
        this.setsRightCol.setMaxWidth(Double.MAX_VALUE);
        this.setsIconContainer.add(this.setsLeftCol, 0, 0);
        this.setsIconContainer.add(this.setsRightCol, 1, 0);
        this.setsLeftColCons.setPercentWidth(50);
        this.setsLeftColCons.setHgrow(Priority.ALWAYS);
        this.setsRightColCons.setPercentWidth(50);
        this.setsRightColCons.setHgrow(Priority.ALWAYS);
        this.setsIconContainer.getColumnConstraints().addAll(this.setsLeftColCons, this.setsRightColCons);

        HBox.setHgrow(this.setsList, Priority.ALWAYS);
        HBox.setHgrow(this.setsIconContainer, Priority.ALWAYS);

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
                for(int i = 0; i < this.studySetCards.size(); i++){
                    if(i % 2 == 0){
                        this.setsLeftCol.getChildren().add(studySetCards.get(i));
                    } else {
                        this.setsRightCol.getChildren().add(studySetCards.get(i));
                    }
                    
                }

                this.setsContainer.getChildren().add(this.setsIconContainer);
                Transitions.standardFadeTransition(this.setsIconContainer);
            } else {
                this.setsLeftCol.getChildren().clear();
                this.setsRightCol.getChildren().clear();
                this.setsList.getChildren().addAll(this.studySetList);
                this.setsContainer.getChildren().add(this.setsList);
                Transitions.standardFadeTransition(this.setsList);
            }

        });

        this.cardViewButton.setSelected(true);
    }
}
