package io.github.knightmareleon.features.test.components.pages;

import java.util.Optional;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.controls.TestGeneralSettings;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.models.TestConfig;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class TestSetupController implements TestPage, StudySetReceiver{

    protected TestNavigator testNavigator;
    private StudySet studySet;

    @FXML private TestGeneralSettings generalSettings;

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        this.generalSettings.setMaxTotalQuestions(this.studySet.getQuestions().size());
    }

        // switch(this.testType){
        //     case FLASHCARD -> {
        //         Label inputStyleLabel = new Label("Input Style:");

        //         inputStyleLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

        //         ToggleButton markerButton = new ToggleButton("Mark as Correct or Incorrect");
        //         ToggleButton textInputButton = new ToggleButton("Text Input");
        //         ToggleGroup inputStyleGroup = new ToggleGroup();

        //         markerButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );
        //         textInputButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );

        //         markerButton.setToggleGroup(inputStyleGroup);
        //         textInputButton.setToggleGroup(inputStyleGroup);

        //         HBox inputStyleContainer = new HBox(24, inputStyleLabel, markerButton, textInputButton);
        //         inputStyleContainer.setAlignment(Pos.CENTER_LEFT);

        //         Label ignoreOptionsLabel = new Label("Ignore on Grading (Text Input) : ");
        //         ignoreOptionsLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

        //         ToggleButton casesButton = new ToggleButton("Case");
        //         ToggleButton punctuationButton = new ToggleButton("Punctuation");
        //         ToggleButton spacesButton = new ToggleButton("Spaces");

        //         casesButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );
        //         punctuationButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );
        //         spacesButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );

        //         HBox ignoreOptionsContainer = new HBox(
        //             24, 
        //             ignoreOptionsLabel,
        //             casesButton,
        //             punctuationButton,
        //             spacesButton
        //         );
        //         ignoreOptionsContainer.setAlignment(Pos.CENTER_LEFT);

        //         inputStyleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
        //             if (newVal == null) oldVal.setSelected(true);
        //         });

        //         textInputButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
        //             casesButton.setDisable(oldVal);
        //             punctuationButton.setDisable(oldVal);
        //             spacesButton.setDisable(oldVal);
        //         });

        //         markerButton.setSelected(true);

        //         casesButton.setDisable(!textInputButton.isSelected());
        //         punctuationButton.setDisable(!textInputButton.isSelected());
        //         spacesButton.setDisable(!textInputButton.isSelected());

        //         this.extraConfigs.put("InputStyle", inputStyleContainer);
        //         this.extraConfigs.put("Ignore", ignoreOptionsContainer);
        //     }

        //     case ENUMERATION -> {
        //         Label ignoreOptionsLabel = new Label("Ignore on Grading: ");
        //         ignoreOptionsLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

        //         ToggleButton casesButton = new ToggleButton("Case");
        //         ToggleButton punctuationButton = new ToggleButton("Punctuation");
        //         ToggleButton spacesButton = new ToggleButton("Spaces");

        //         casesButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );
        //         punctuationButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );
        //         spacesButton.getStyleClass().addAll(
        //             StandardStyleClass.COMPONENT_BG,
        //             StandardStyleClass.WHITE_BORDER,
        //             StandardStyleClass.STANDARD_FONT
        //         );

        //         HBox ignoreOptionsContainer = new HBox(
        //             24, 
        //             ignoreOptionsLabel,
        //             casesButton,
        //             punctuationButton,
        //             spacesButton
        //         );
        //         ignoreOptionsContainer.setAlignment(Pos.CENTER_LEFT);

        //         this.extraConfigs.put("Ignore", ignoreOptionsContainer);
        //     }
        //     default -> {

        //     }
        // }

        // if(!extraConfigs.isEmpty()) {
        //     Label extraConfigLabel = new Label(this.testType.getName() + " Configurations");
        //     extraConfigLabel.getStyleClass().add(StandardStyleClass.STANDARD_HEADER_FONT);
        //     this.configurationContainer.getChildren().add(extraConfigLabel);
        //     this.extraConfigs.forEach((key, node) -> {
        //         this.configurationContainer.getChildren().add(node);
        //     });
        // }

        // this.configurationContainer.getChildren().addAll(space, startButton);
        //}

    @FXML
    protected abstract void onBackPageClicked();

    @FXML
    protected abstract void onStartClicked();

    protected final TestConfig.Builder createGeneralTestConfig(TestType testType){
        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start");
        alert.setHeaderText("Starting Test for " + this.studySet.getTitle());
        alert.setContentText("Are you sure you want to start?");
        Optional<ButtonType> alertResult = alert.showAndWait();
        if(alertResult.isPresent() && ButtonType.OK == alertResult.get()){
            return new TestConfig.Builder(
                    testType,
                    this.studySet,
                    this.generalSettings.totalQuestions(),
                    this.generalSettings.timeToggled(),
                    this.generalSettings.timeSetting(),
                    this.generalSettings.shuffleToggled(),
                    this.generalSettings.continuousToggled()
            );
        }
        return null;
    }
            // if(this.testType == TestType.MULTIPLE_CHOICE 
            //     && ((ToggleButton)this.extraConfigs.get("Randomized")).isSelected())
            //     testConfigBuilder.setRandomized(true);
            // if(this.testType == TestType.FLASHCARD && 
            //     ((ToggleButton)((HBox)this.extraConfigs.get("InputStyle"))
            //     .getChildren().get(2)).isSelected())
            // {
            //     testConfigBuilder.setIfTextInput(true);
            //     HBox ignoreOptionsContainer = (HBox)this.extraConfigs.get("Ignore");
            //     ToggleButton casesButton = (ToggleButton) ignoreOptionsContainer.getChildren().get(1);
            //     ToggleButton punctuationButton = (ToggleButton) ignoreOptionsContainer.getChildren().get(2);
            //     ToggleButton spacesButton = (ToggleButton) ignoreOptionsContainer.getChildren().get(3);

            //     testConfigBuilder.setIgnoreCases(casesButton.isSelected());
            //     testConfigBuilder.setIgnorePunctuation(punctuationButton.isSelected());
            //     testConfigBuilder.setIgnoreSpaces(spacesButton.isSelected());
            // }
            // if(this.testType == TestType.ENUMERATION){
            //     HBox ignoreContainer = (HBox)this.extraConfigs.get("Ignore");
            //     ToggleButton casesButton = (ToggleButton) ignoreContainer.getChildren().get(1);
            //     ToggleButton punctuationButton = (ToggleButton) ignoreContainer.getChildren().get(2);
            //     ToggleButton spacesButton = (ToggleButton) ignoreContainer.getChildren().get(3);

            //     testConfigBuilder.setIgnoreCases(casesButton.isSelected());
            //     testConfigBuilder.setIgnorePunctuation(punctuationButton.isSelected());
            //     testConfigBuilder.setIgnoreSpaces(spacesButton.isSelected());
            // }
    //         this.testNavigator.show(TestPageURL.PLAY, testConfigBuilder.build());
    //     }
    // }
}