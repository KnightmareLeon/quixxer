package io.github.knightmareleon.features.test.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.TestService;
import io.github.knightmareleon.features.test.TestType;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import io.github.knightmareleon.shared.ui.controls.SetCardForm;
import io.github.knightmareleon.shared.ui.controls.SetListForm;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TestSetsPickerController implements TestPage{

    private TestNavigator navigator;
    private final TestService testService;

    private TestType type;
    private List<StudySet> studySets;
    private final List<SetCardForm> studySetCards = new ArrayList<>();
    private final List<SetListForm> studySetList = new ArrayList<>();

    private final VBox setsLeftCol = new VBox(24);
    private final VBox setsRightCol = new VBox(24);
    private final VBox setsList = new VBox(24);

    @FXML private IconToggleButton cardViewButton;
    @FXML private IconToggleButton listViewButton;

    @FXML private HBox setsContainer;
    public TestSetsPickerController(TestService testService){
        this.testService = testService;
    }

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.navigator = testNavigator;
    }


    public void setTestType(TestType type){
        this.type = type;
        switch(this.type){
            case TestType.MULTIPLE_CHOICE:
                break;
            case TestType.FLASHCARD:
                break;
            case TestType.MATCHING_TYPE:
                break;
            case TestType.ENUMERATION:
                break;
            case TestType.TRUE_OR_FALSE:
                this.studySets = this.testService.getSetsWithTrueOrFalseOnly(
                    1).getValue();
                break;
            case TestType.COMBINED:
                break;
            default:
                break;
        }
        for(StudySet studySet : this.studySets){
            SetCardForm setCardForm = new SetCardForm(
                studySet.getimgpath().equals("default") ? 
                "dashicons-book-alt" : studySet.getimgpath(), 
                studySet.getTitle(), 
                studySet.getSubject(),
                studySet.getQuestions().size()
            );
            SetListForm setListForm = new SetListForm(                
                studySet.getimgpath().equals("default") ? 
                "dashicons-book-alt" : studySet.getimgpath(), 
                studySet.getTitle(), 
                studySet.getSubject(),
                studySet.getQuestions().size()
            );

            studySetCards.add(setCardForm);
            studySetList.add(setListForm);
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
                    if(i % 2 == 0){
                        this.setsLeftCol.getChildren().add(studySetCards.get(i));
                    } else {
                        this.setsRightCol.getChildren().add(studySetCards.get(i));
                    }
                    
                }

                if(studySetCards.size() == 1){
                    SetCardForm blank = new SetCardForm("dashicons-book-alt", null, null, 0);
                    blank.setVisible(false);
                    this.setsRightCol.getChildren().add(blank);
                }

                this.setsContainer.getChildren().addAll(this.setsLeftCol, this.setsRightCol);
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
    public void initialize(){

    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show("main");
    }
}
