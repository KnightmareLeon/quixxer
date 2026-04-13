package io.github.knightmareleon.features.test.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.TestService;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.TestTypeReceiver;
import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import io.github.knightmareleon.shared.ui.controls.Paginator;
import io.github.knightmareleon.shared.ui.controls.SetCardForm;
import io.github.knightmareleon.shared.ui.controls.SetListForm;
import io.github.knightmareleon.shared.ui.controls.SetsLister;
import javafx.fxml.FXML;

public class TestSetsPickerController implements TestPage, TestTypeReceiver{

    private TestNavigator navigator;
    private final TestService testService;

    private TestType type;

    private List<StudySet> studySets;
    private final List<SetCardForm> studySetCards = new ArrayList<>();
    private final List<SetListForm> studySetList = new ArrayList<>();

    @FXML private SetsLister setsLister;
    @FXML private IconToggleButton cardViewButton;
    @FXML private IconToggleButton listViewButton;

    @FXML private Paginator paginator;
    private int currentPage = 1;

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
        this.type = type;
        this.createStudySetComponents();
        this.setsLister.init(
            this.studySetCards, 
            this.studySetList, 
            this.cardViewButton, 
            this.listViewButton
        );
        this.paginator.init(this.testService.getTotalPagesByTest(this.type));
        this.paginator.setFirstPageButtonAction(e -> {
            this.currentPage = 1;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.paginator.setPrevPageButtonAction(e -> {
            this.currentPage--;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.paginator.setNextPageButtonAction(e -> {
            this.currentPage++;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.paginator.setLastPageButtonAction(e -> {
            this.currentPage = this.testService.getTotalPagesByTest(this.type);
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.paginator.addPageFieldListener((obs, oldVal, newVal) -> {
            int newPage = Integer.parseInt(newVal);
            this.currentPage = newPage;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
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

    private void createStudySetComponents(){
        this.studySets = this.testService.getSetsByTestType(
            this.currentPage, this.type)
            .getValue();
        this.studySetCards.clear();
        this.studySetList.clear();
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
                this.onSetClicked(studySet, this.type);
            });
            setListForm.setOnMouseClicked(e -> {
                this.onSetClicked(studySet, this.type);
            });

            this.studySetCards.add(setCardForm);
            this.studySetList.add(setListForm);
        }
    }
}
