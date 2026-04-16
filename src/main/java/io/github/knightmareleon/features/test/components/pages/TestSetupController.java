package io.github.knightmareleon.features.test.components.pages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.TestTypeReceiver;
import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.constants.StandardStyleClass;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.models.TestConfig;
import io.github.knightmareleon.shared.ui.controls.NaturalNumberField;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.Converter;
import io.github.knightmareleon.shared.utils.StudySetReceiver;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TestSetupController implements TestPage, StudySetReceiver, TestTypeReceiver{

    private TestNavigator testNavigator;
    private TestType testType;
    private StudySet studySet;

    @FXML private Label setupTitle;
    @FXML private NaturalNumberField totalQuestions;
    @FXML private Label totalQuestionsMax;

    @FXML private VBox configurationContainer;

    @FXML private ToggleButton timeToggleButton;
    @FXML private ToggleButton thirtySecButton;
    @FXML private ToggleButton oneMinButton;
    @FXML private ToggleButton threeMinButton;
    @FXML private ToggleButton fiveMinButton;
    @FXML private ToggleButton tenMinButton;

    private final ToggleGroup timeToggleGroup = new ToggleGroup();
    @FXML private ToggleButton shuffleToggleButton;
    @FXML private ToggleButton continuousToggleButton;

    private final HashMap<String,Node> extraConfigs = new LinkedHashMap<>(5);

    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveStudySet(StudySet studySet) {
        this.studySet = studySet;
        int maxTotalQuestions = this.studySet.getQuestions().size();
        this.totalQuestions.setMaxNumber(maxTotalQuestions);
        this.totalQuestions.setTextToMax();
        this.totalQuestionsMax.setText(" / " + maxTotalQuestions);
    }

    @Override
    public void receiveTestType(TestType testType) {
        this.testType = testType;
        
        this.setupTitle.setText(testType.getName() + " Setup");

        Region space = new Region();
        VBox.setVgrow(space, Priority.ALWAYS);

        Button startButton = new Button("Start");
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.getStyleClass().addAll(
            StandardStyleClass.COMPONENT_BG,
            StandardStyleClass.STANDARD_FONT,
            StandardStyleClass.BORDER_RADIUS_15
        );
        startButton.setOnAction(e -> {this.onStartClicked();});

        switch(this.testType){
            case MULTIPLE_CHOICE -> {
                ToggleButton randomizedButton = new ToggleButton("Randomized");
                randomizedButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );
                this.extraConfigs.put("Randomized", randomizedButton);
            }
            case FLASHCARD -> {
                Label inputStyleLabel = new Label("Input Style:");

                inputStyleLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

                ToggleButton markerButton = new ToggleButton("Mark as Correct or Incorrect");
                ToggleButton textInputButton = new ToggleButton("Text Input");
                ToggleGroup inputStyleGroup = new ToggleGroup();

                markerButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );
                textInputButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );

                markerButton.setToggleGroup(inputStyleGroup);
                textInputButton.setToggleGroup(inputStyleGroup);

                HBox inputStyleContainer = new HBox(24, inputStyleLabel, markerButton, textInputButton);
                inputStyleContainer.setAlignment(Pos.CENTER_LEFT);

                Label ignoreOptionsLabel = new Label("Ignore on Grading (Text Input) : ");
                ignoreOptionsLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

                ToggleButton casesButton = new ToggleButton("Case");
                ToggleButton punctuationButton = new ToggleButton("Punctuation");
                ToggleButton spacesButton = new ToggleButton("Spaces");

                casesButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );
                punctuationButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );
                spacesButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );

                HBox ignoreOptionsContainer = new HBox(
                    24, 
                    ignoreOptionsLabel,
                    casesButton,
                    punctuationButton,
                    spacesButton
                );
                ignoreOptionsContainer.setAlignment(Pos.CENTER_LEFT);

                inputStyleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
                    if (newVal == null) oldVal.setSelected(true);
                });

                textInputButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    casesButton.setDisable(oldVal);
                    punctuationButton.setDisable(oldVal);
                    spacesButton.setDisable(oldVal);
                });

                markerButton.setSelected(true);

                casesButton.setDisable(!textInputButton.isSelected());
                punctuationButton.setDisable(!textInputButton.isSelected());
                spacesButton.setDisable(!textInputButton.isSelected());

                this.extraConfigs.put("InputStyle", inputStyleContainer);
                this.extraConfigs.put("Ignore", ignoreOptionsContainer);
            }

            case ENUMERATION -> {
                Label ignoreOptionsLabel = new Label("Ignore on Grading: ");
                ignoreOptionsLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

                ToggleButton casesButton = new ToggleButton("Case");
                ToggleButton punctuationButton = new ToggleButton("Punctuation");
                ToggleButton spacesButton = new ToggleButton("Spaces");

                casesButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );
                punctuationButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );
                spacesButton.getStyleClass().addAll(
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.WHITE_BORDER,
                    StandardStyleClass.STANDARD_FONT
                );

                HBox ignoreOptionsContainer = new HBox(
                    24, 
                    ignoreOptionsLabel,
                    casesButton,
                    punctuationButton,
                    spacesButton
                );
                ignoreOptionsContainer.setAlignment(Pos.CENTER_LEFT);

                this.extraConfigs.put("Ignore", ignoreOptionsContainer);
            }
            default -> {

            }
        }

        if(!extraConfigs.isEmpty()) {
            Label extraConfigLabel = new Label(this.testType.getName() + " Configurations");
            extraConfigLabel.getStyleClass().add(StandardStyleClass.STANDARD_HEADER_FONT);
            this.configurationContainer.getChildren().add(extraConfigLabel);
            this.extraConfigs.forEach((key, node) -> {
                this.configurationContainer.getChildren().add(node);
            });
        }

        this.configurationContainer.getChildren().addAll(space, startButton);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.testNavigator.show(TestPageURL.SETS, this.testType);
    }

    @FXML
    public void initialize(){
        this.timeToggleButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
            this.onTimeSelected(oldVal);
        });

        this.onTimeSelected(!this.timeToggleButton.isSelected());

        thirtySecButton.setToggleGroup(this.timeToggleGroup);
        oneMinButton.setToggleGroup(this.timeToggleGroup);
        threeMinButton.setToggleGroup(this.timeToggleGroup);
        fiveMinButton.setToggleGroup(this.timeToggleGroup);
        tenMinButton.setToggleGroup(this.timeToggleGroup);
        timeToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) oldVal.setSelected(true);
        });
        thirtySecButton.setSelected(true);

    }

    private void onTimeSelected(boolean selected){
        this.thirtySecButton.setDisable(selected);
        this.oneMinButton.setDisable(selected);
        this.threeMinButton.setDisable(selected);
        this.fiveMinButton.setDisable(selected);
        this.tenMinButton.setDisable(selected);
    }

    private void onStartClicked(){
        Alert alert = new StandardAlert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start");
        alert.setHeaderText("Starting Test for " + this.studySet.getTitle());
        alert.setContentText("Are you sure you want to start?");
        Optional<ButtonType> alertResult = alert.showAndWait();
        if(alertResult.isPresent() && alertResult.get() == ButtonType.OK){
            TestConfig.Builder testConfigBuilder = new TestConfig.Builder(
                this.testType, 
                this.studySet, 
                Integer.parseInt(this.totalQuestions.getText()), 
                this.timeToggleButton.isSelected(), 
                this.timeToggleButton.isSelected() ? 
                Converter.toTimeSetting(
                    ((ToggleButton)timeToggleGroup.getSelectedToggle()).getText()
                ) : null,
                this.continuousToggleButton.isSelected(),
                this.shuffleToggleButton.isSelected()
            );
            
            if(this.testType == TestType.MULTIPLE_CHOICE 
                && ((ToggleButton)this.extraConfigs.get("Randomized")).isSelected())
                testConfigBuilder.setRandomized(true);
            if(this.testType == TestType.FLASHCARD && 
                ((ToggleButton)((HBox)this.extraConfigs.get("InputStyle"))
                .getChildren().get(2)).isSelected())
            {
                testConfigBuilder.setIfTextInput(true);
                HBox ignoreOptionsContainer = (HBox)this.extraConfigs.get("Ignore");
                ToggleButton casesButton = (ToggleButton) ignoreOptionsContainer.getChildren().get(1);
                ToggleButton punctuationButton = (ToggleButton) ignoreOptionsContainer.getChildren().get(2);
                ToggleButton spacesButton = (ToggleButton) ignoreOptionsContainer.getChildren().get(3);

                testConfigBuilder.setIgnoreCases(casesButton.isSelected());
                testConfigBuilder.setIgnorePunctuation(punctuationButton.isSelected());
                testConfigBuilder.setIgnoreSpaces(spacesButton.isSelected());
            }
            if(this.testType == TestType.ENUMERATION){
                HBox ignoreContainer = (HBox)this.extraConfigs.get("Ignore");
                ToggleButton casesButton = (ToggleButton) ignoreContainer.getChildren().get(1);
                ToggleButton punctuationButton = (ToggleButton) ignoreContainer.getChildren().get(2);
                ToggleButton spacesButton = (ToggleButton) ignoreContainer.getChildren().get(3);

                testConfigBuilder.setIgnoreCases(casesButton.isSelected());
                testConfigBuilder.setIgnorePunctuation(punctuationButton.isSelected());
                testConfigBuilder.setIgnoreSpaces(spacesButton.isSelected());
            }
            this.testNavigator.show(TestPageURL.PLAY, testConfigBuilder.build());
        }
    }
}