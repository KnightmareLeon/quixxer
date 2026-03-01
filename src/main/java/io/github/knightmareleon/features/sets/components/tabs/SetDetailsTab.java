package io.github.knightmareleon.features.sets.components.tabs;

import java.io.IOException;

import io.github.knightmareleon.features.sets.components.controls.SetDetailsCard;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class SetDetailsTab extends VBox{

    private StudySet studySet;

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

        FXMLLoader loader;
        loader = new FXMLLoader(
                getClass().getResource("SetDetailsTab.fxml")
        );
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize(){
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
}
