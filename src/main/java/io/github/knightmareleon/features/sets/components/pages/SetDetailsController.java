package io.github.knightmareleon.features.sets.components.pages;

import java.util.Optional;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.components.tabs.SetDetailsTab;
import io.github.knightmareleon.features.sets.components.tabs.SetQuestionsTab;
import io.github.knightmareleon.features.sets.constants.SetsPageURL;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.IconToggleButton;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class SetDetailsController extends VBox implements SetsPage, StudySetReceiver {

    private final SetsService setsService;
    private SetsNavigator navigator;
    private StudySet studySet;

    private final ToggleGroup setTabs = new ToggleGroup();
    @FXML private IconToggleButton detailsToggleButton;
    @FXML private IconToggleButton questionToggleButton;

    @FXML private Label setName;
    @FXML private VBox tabsContainer;

    private SetDetailsTab detailsTab;
    private SetQuestionsTab questionsTab;

    public SetDetailsController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void receiveStudySet(StudySet data){
        this.setStudySet(data);
    }

    private void setStudySet(StudySet studySet){
        this.studySet = studySet;
        this.setName.setText(this.studySet.getTitle());

        this.detailsTab = new SetDetailsTab(studySet);
        this.questionsTab = new SetQuestionsTab(studySet, e -> {onAddQuestionClicked();});

        this.detailsToggleButton.setToggleGroup(this.setTabs);
        this.questionToggleButton.setToggleGroup(this.setTabs);

        setTabs.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
                return;
            }
            if (oldVal == null && !this.tabsContainer.getChildren().isEmpty()) {
                return;
            }
            this.tabsContainer.getChildren().clear();
            if((IconToggleButton) newVal == this.detailsToggleButton){
                this.tabsContainer.getChildren().add(this.detailsTab);
                Transitions.standardFadeTransition(this.detailsTab);
            } else {
                this.tabsContainer.getChildren().add(this.questionsTab);
                Transitions.standardFadeTransition(this.questionsTab);
            }

        });

        this.detailsToggleButton.setSelected(true);
        this.questionsTab.setTabMaxHeight(Double.MAX_VALUE);
    }

    @FXML
    public void initialize(){
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked() {
        navigator.show(SetsPageURL.MAIN);
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
            this.setsService.deleteStudySet(this.studySet.getId());
            navigator.show(SetsPageURL.MAIN);
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void onUpdateClicked(){
        this.navigator.show(SetsPageURL.DETAILS_UPDATE, this.studySet);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onAddQuestionClicked(){
        this.navigator.show(SetsPageURL.ADD_QUESTION, this.studySet);
    }
}
