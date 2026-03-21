package io.github.knightmareleon.features.sets.components.pages;

import java.util.ArrayList;
import java.util.Optional;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.features.sets.constants.SetsConstants;
import io.github.knightmareleon.features.sets.constants.SetsPageURL;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.Result;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SetUpdateDetailsController implements SetsPage, StudySetReceiver {

    private SetsNavigator setsNavigator;
    private final SetsService setsService;
    private StudySet studySet;

    @FXML private Label updateSetTitle;

    @FXML private Label titleErrorLabel;
    @FXML private TextField setTitle;
    
    @FXML private ComboBox<String> subjectPicker;

    public SetUpdateDetailsController(SetsService setsService){
        this.setsService = setsService;
    }

    @Override
    public void setSetsNavigator(SetsNavigator navigator) {
        this.setsNavigator = navigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        initWithStudySet();
    }

    @FXML
    public void initialize(){
        this.subjectPicker.setItems(SetsConstants.SUBJECTS);
    }

    private void initWithStudySet(){
        this.updateSetTitle.setText("Update " + this.studySet.getTitle() + " Details");
        this.setTitle.setText(this.studySet.getTitle());
        this.subjectPicker.setValue(this.studySet.getSubject());
    }

    @FXML
    @SuppressWarnings("unused")
    private void saveSet(){
        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Confirmation");
        alert.setHeaderText("Updating Study Set " + this.studySet.getTitle());
        alert.setContentText("Please confirm if all the details for updating is complete.");

        Optional<ButtonType> alertResult = alert.showAndWait();

        if(alertResult.isPresent() && alertResult.get() == ButtonType.CANCEL) return;

        StudySet updatedStudySet = new StudySet(
            this.studySet.getId(),
            this.setTitle.getText(),
            this.subjectPicker.getValue(),
            this.studySet.getimgpath(),
            this.studySet.totalTakes(),
            new ArrayList<>(studySet.getQuestions()),
            this.studySet.getDateCreatedOn(),
            this.studySet.getDateLastTakeOn()
        );

        Result<String> updateResult = this.setsService.updateStudySetDetails(updatedStudySet);

        if(updateResult.isSuccess()){
            this.setsNavigator.show(SetsPageURL.DETAILS, updatedStudySet);
            return;
        } 

        Alert errorAlert = new StandardAlert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Update Error");

        String errorMsg = updateResult.getErrorMessages().get(0);
        String headerText;
        String contentText = "";

        boolean missingFields = false;

        switch(errorMsg){
            case SetsConstants.DATABASE_ERROR -> {
                headerText = "Database Error.";
                contentText = "An error occured in the database.";
            }
            case SetsConstants.DUPLICATE_STUDY_SET_ERROR -> {
                headerText = "Unique Study Set";
                contentText = String.format(
                    "Study Set %s with subject %s already exists"
                    , updatedStudySet.getTitle(), updatedStudySet.getSubject());
            }
            case SetsConstants.MISSING_TITLE_ERROR -> {
                headerText = "Missing required fields.";
                contentText = "Please fill in all required fields.";
                missingFields = true;
            }
            default -> {
                headerText = "Unknown Error";
            }
        }

        errorAlert.setHeaderText(headerText);
        errorAlert.setContentText(contentText);
        errorAlert.showAndWait();

        this.titleErrorLabel.setVisible(missingFields);

    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.setsNavigator.show(SetsPageURL.DETAILS, this.studySet);
    }
    
}