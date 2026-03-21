package io.github.knightmareleon.features.sets.components.tabs;

import io.github.knightmareleon.features.sets.components.controls.SetDetailsCard;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class SetDetailsTab extends VBox{

    private final StudySet studySet;

    @FXML private SetDetailsCard subjectCard;
    @FXML private SetDetailsCard createdOnCard;
    @FXML private SetDetailsCard lastTakenOnCard;
    @FXML private SetDetailsCard totalQuestionsCard;
    @FXML private SetDetailsCard identificationTotalCard;
    @FXML private SetDetailsCard enumerationTotalCard;
    @FXML private SetDetailsCard tofTotalCard;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetDetailsTab(StudySet studySet) {

        this.studySet = studySet;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("SetDetailsTab.fxml")
        );
        ControllerRootSetter.set(this, loader);
    }

    @FXML
    public void initialize(){
        this.subjectCard.setDataText(this.studySet.getSubject());
        this.createdOnCard.setDataText(this.studySet.getDateCreatedOn().toString());
        this.lastTakenOnCard.setDataText(this.studySet.getDateLastTakeOn() == null ? 
            "Not yet taken." : this.studySet.getDateLastTakeOn().toString());
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
}
