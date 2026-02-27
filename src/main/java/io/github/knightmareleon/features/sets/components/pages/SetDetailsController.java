package io.github.knightmareleon.features.sets.components.pages;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.SetsPage;
import io.github.knightmareleon.features.sets.components.controls.SetDetailsCard;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetDetailsController implements SetsPage {

    private final SetsService setsService;
    private SetsNavigator navigator;
    private StudySet studySet;

    @FXML private Label setName;
    @FXML private SetDetailsCard subjectCard;
    @FXML private SetDetailsCard createdOnCard;
    @FXML private SetDetailsCard lastTakenOnCard;
    @FXML private SetDetailsCard totalQuestionsCard;
    @FXML private SetDetailsCard identificationTotalCard;
    @FXML private SetDetailsCard enumerationTotalCard;
    @FXML private SetDetailsCard tofTotalCard;

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
                case QuestionType.IDENTIFICATION:
                    identificationTotal++;
                    break;
                case QuestionType.ENUMERATION:
                    enumerationTotal++;
                    break;
                default:
                    tofTotal++;
            }
        }

        this.identificationTotalCard.setDataText(identificationTotal + "");
        this.enumerationTotalCard.setDataText(enumerationTotal + "");
        this.tofTotalCard.setDataText(tofTotal + "");
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
