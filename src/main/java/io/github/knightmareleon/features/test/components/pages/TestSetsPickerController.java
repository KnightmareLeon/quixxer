package io.github.knightmareleon.features.test.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.TestService;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.TestTypeReceiver;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import io.github.knightmareleon.shared.ui.controls.SetCardForm;
import io.github.knightmareleon.shared.ui.controls.SetListForm;
import io.github.knightmareleon.shared.ui.controls.SetsLister;
import javafx.fxml.FXML;

public class TestSetsPickerController implements TestPage, TestTypeReceiver{

    private TestNavigator navigator;
    private final TestService testService;

    private List<StudySet> studySets;
    private final List<SetCardForm> studySetCards = new ArrayList<>();
    private final List<SetListForm> studySetList = new ArrayList<>();

    @FXML private SetsLister setsLister;
    @FXML private IconToggleButton cardViewButton;
    @FXML private IconToggleButton listViewButton;

    public TestSetsPickerController(TestService testService){
        this.testService = testService;
    }

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.navigator = testNavigator;
    }

    @Override
    public void receiveTestType(TestType testType){
        this.setTestType(testType);
    }

    private void setTestType(TestType type){
        this.studySets = this.testService.getSetsByTestType(
            1, type)
            .getValue();

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

            setCardForm.setOnMouseClicked(e -> {
                this.onSetClicked(studySet, type);
            });
            setListForm.setOnMouseClicked(e -> {
                this.onSetClicked(studySet, type);
            });

            this.studySetCards.add(setCardForm);
            this.studySetList.add(setListForm);
        }

        this.setsLister.init(
            this.studySetCards, 
            this.studySetList, 
            this.cardViewButton, 
            this.listViewButton
        );
    }

    @FXML
    public void initialize(){

    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show(TestPageURL.MAIN);
    }

    private void onSetClicked(StudySet studySet, TestType type){
        navigator.show(TestPageURL.SETUP, studySet, type);
    }
}
