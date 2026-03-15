package io.github.knightmareleon.features.test.components.pages;

import io.github.knightmareleon.features.test.components.TestDataReceiver;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.TestData;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TestPlayerController implements TestPage, TestDataReceiver{

    private TestNavigator testNavigator;
    private TestData testData;

    @FXML private Label testPlayerTitle;
    @FXML private BorderPane mainContentPane;
    @Override
    public void setTestNavigator(TestNavigator testNavigator) {
        this.testNavigator = testNavigator;
    }

    @Override
    public void receiveTestData(TestData testData) {
        this.testData = testData;
        this.initWithTestData();
    }

    private void initWithTestData(){
        this.testPlayerTitle.setText(this.testData.getStudySetTitle() + " Test");
        this.handleNextQuestion(0);
    }

    private void handleNextQuestion(int nextQuestionIndex){
        if(nextQuestionIndex > this.testData.getQuestionsUsed().size() - 1){
            handleEnd();
            return;
        }
        Question question = this.testData.getQuestionsUsed().get(nextQuestionIndex);
        Label questionLabel = new Label(question.getDescription());
        questionLabel.getStyleClass().add("standard-font");
        this.mainContentPane.setCenter(questionLabel);
        this.addAnswerFields(question, nextQuestionIndex);
    }

    private void handleQuestionResult(Question question, boolean correct, int currentIndex){
        System.out.println(correct);
        if(correct) this.testData.incrementScore();
        this.handleNextQuestion(currentIndex + 1);
    }

    private void handleEnd(){
        Label scoreLabel = new Label("Final Score: " + this.testData.getScore());
        scoreLabel.getStyleClass().add("standard-font");
        this.mainContentPane.setCenter(scoreLabel);
        this.mainContentPane.setBottom(null);
    }

    private void addAnswerFields(Question question, int currentIndex){
        switch(this.testData.getType()){
            case TestType.TRUE_OR_FALSE -> {
                
                HBox trueFalseContainer = new HBox();
                ToggleButton trueButton = new ToggleButton("True");
                ToggleButton falseButton = new ToggleButton("False");
                ToggleGroup trueFalseGroup = new ToggleGroup();
                trueButton.setToggleGroup(trueFalseGroup);
                falseButton.setToggleGroup(trueFalseGroup);
                HBox.setHgrow(trueButton, Priority.ALWAYS);
                HBox.setHgrow(falseButton, Priority.ALWAYS);
                trueButton.getStyleClass().addAll("standard-font","component-standard-bg","border-radius-15","white-border");
                falseButton.getStyleClass().addAll("standard-font","component-standard-bg","border-radius-15","white-border");
                trueButton.setMaxWidth(Double.MAX_VALUE);
                falseButton.setMaxWidth(Double.MAX_VALUE);
                trueFalseContainer.getChildren().addAll(trueButton,falseButton);
                trueFalseContainer.setFillHeight(true);
                trueFalseContainer.setSpacing(24);
                Button submitButton = new Button("Submit");
                submitButton.getStyleClass().addAll("standard-font","component-standard-bg","border-radius-15");
                submitButton.setMaxWidth(Double.MAX_VALUE);
                submitButton.setOnAction(e -> {
                    if(trueFalseGroup.getSelectedToggle() == null){
                        Alert alert = new StandardAlert(Alert.AlertType.ERROR);
                        alert.setTitle("No answer selected");
                        alert.setHeaderText("No answer selected");
                        alert.setContentText("Please choose either true or false.");
                        alert.showAndWait();
                    } else {
                        handleQuestionResult(
                            question,
                            (trueFalseGroup.getSelectedToggle() == trueButton) ==
                            (question.getChoices().get(0).isAnswer()),
                            currentIndex
                        );
                    }
                });
                VBox answerFieldContainer = new VBox(24, trueFalseContainer, submitButton);
                answerFieldContainer.setFillWidth(true);
                this.mainContentPane.setBottom(answerFieldContainer);
            }
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.testNavigator.show(TestPageURL.SETS, this.testData.getType());
    }
}