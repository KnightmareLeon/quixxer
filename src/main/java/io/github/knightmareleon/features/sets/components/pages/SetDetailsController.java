package io.github.knightmareleon.features.sets.components.pages;

import java.util.Optional;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.SetDetailsCard;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class SetDetailsController extends VBox implements SetsPage {

    private final SetsService setsService;
    private SetsNavigator navigator;
    private StudySet studySet;

    private final ToggleGroup setTabs = new ToggleGroup();
    @FXML private IconToggleButton detailsToggleButton;
    @FXML private IconToggleButton questionToggleButton;

    @FXML private Label setName;
    @FXML private SetDetailsCard subjectCard;
    @FXML private SetDetailsCard createdOnCard;
    @FXML private SetDetailsCard lastTakenOnCard;
    @FXML private SetDetailsCard totalQuestionsCard;
    @FXML private SetDetailsCard identificationTotalCard;
    @FXML private SetDetailsCard enumerationTotalCard;
    @FXML private SetDetailsCard tofTotalCard;

    @FXML private VBox detailsTab;

    public SetDetailsController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    public void setStudySet(StudySet studySet){
        System.out.println("Retrieved study set:\n" + studySet );
        this.studySet = studySet;
        this.setName.setText(this.studySet.getTitle());
        this.subjectCard.setDataText(this.studySet.getSubject());
        this.createdOnCard.setDataText(this.studySet.getDateCreatedOn().toString());
        this.lastTakenOnCard.setDataText(this.studySet.getDataLastTakeOn() == null ? 
            "Not yet taken." : this.studySet.getDataLastTakeOn().toString());
        this.totalQuestionsCard.setDataText(this.studySet.getQuestions().size() + "");
        
        int identificationTotal = 0;
        int enumerationTotal = 0;
        int tofTotal = 0;

        for(Question question : this.studySet.getQuestions()){
            switch(question.getType()){
                case QuestionType.IDENTIFICATION -> identificationTotal++;
                case QuestionType.ENUMERATION -> enumerationTotal++;
                default -> tofTotal++;
            }
        }

        this.identificationTotalCard.setDataText(identificationTotal + "");
        this.enumerationTotalCard.setDataText(enumerationTotal + "");
        this.tofTotalCard.setDataText(tofTotal + "");
    }

    @FXML
    public void initialize(){
        this.detailsToggleButton.setToggleGroup(this.setTabs);
        this.questionToggleButton.setToggleGroup(this.setTabs);

        setTabs.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
                return;
            }
            if((IconToggleButton) newVal == this.detailsToggleButton){
                this.detailsTab.setVisible(true);
            } else {
                this.detailsTab.setVisible(false);
            }

        });

        this.detailsToggleButton.setSelected(true);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show("main");
    }

    @FXML
    @SuppressWarnings("unused")
    private void onDeleteClicked(){
        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Deleting Study Set " + this.studySet.getTitle());
        alert.setContentText("Are you sure you want to delete this?");
        Optional<ButtonType> alertResult = alert.showAndWait();
        if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
            this.setsService.deleteStudyResult(this.studySet.getId());
            navigator.show("main");
        }
    }
}
