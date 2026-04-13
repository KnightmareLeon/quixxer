package io.github.knightmareleon.features.sets.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.constants.SetsPageURL;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import io.github.knightmareleon.shared.ui.controls.Paginator;
import io.github.knightmareleon.shared.ui.controls.SetCardForm;
import io.github.knightmareleon.shared.ui.controls.SetListForm;
import io.github.knightmareleon.shared.ui.controls.SetsLister;
import javafx.fxml.FXML;

public class SetsMainController implements SetsPage{

    private SetsNavigator navigator;
    private final SetsService setsService;

    private List<StudySet> studySets;
    private final List<SetCardForm> studySetCards = new ArrayList<>();
    private final List<SetListForm> studySetList = new ArrayList<>();

    @FXML private SetsLister setsLister;
    @FXML private IconToggleButton cardViewButton;
    @FXML private IconToggleButton listViewButton;

    @FXML private Paginator pagination;
    private int currentPage = 1;

    public SetsMainController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @FXML
    public void initialize(){
        this.createStudySetComponents();
        this.setsLister.init(
            this.studySetCards, 
            this.studySetList,
            this.cardViewButton,
            this.listViewButton
        );
        this.pagination.init(this.setsService.getTotalPages());
        this.pagination.setFirstPageButtonAction(e -> {
            this.currentPage = 1;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.pagination.setPrevPageButtonAction(e -> {
            this.currentPage--;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.pagination.setNextPageButtonAction(e -> {
            this.currentPage++;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.pagination.setLastPageButtonAction(e -> {
            this.currentPage = this.setsService.getTotalPages();
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
        this.pagination.addPageFieldListener((obs, oldVal, newVal) -> {
            int newPage = Integer.parseInt(newVal);
            this.currentPage = newPage;
            this.createStudySetComponents();
            this.setsLister.refresh();
        });
    }

    @FXML
    @SuppressWarnings("unused")
    private void onCreateSetsClicked() {
        navigator.show(SetsPageURL.CREATE);
    }

    private void onSetClicked(StudySet studySet){
        navigator.show(SetsPageURL.DETAILS, studySet);
    }

    private void createStudySetComponents(){
        this.studySets = this.setsService.getStudySets(this.currentPage).getValue();
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
                this.onSetClicked(studySet);
            });
            setListForm.setOnMouseClicked(e -> {
                this.onSetClicked(studySet);
            });

            studySetCards.add(setCardForm);
            studySetList.add(setListForm);
        }
    }
}