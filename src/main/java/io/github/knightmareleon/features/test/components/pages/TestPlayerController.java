package io.github.knightmareleon.features.test.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.components.TestDataReceiver;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.TestData;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.Converter;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class TestPlayerController implements TestPage, TestDataReceiver{

    private TestNavigator testNavigator;
    private TestData testData;

    @FXML private Label testPlayerTitle;
    @FXML private HBox mainContentHeader;
    @FXML private Label mainContentHeaderLabel;
    private final Label timeLabel = new Label();

    @FXML private ProgressBar incorrectProgressBar;
    @FXML private ProgressBar correctProgressBar;

    @FXML private BorderPane mainContentPane;
    @FXML private VBox centralContent;

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

        if(this.testData.isTimed()){
            Region space = new Region();
            space.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(space, Priority.ALWAYS);

            this.timeLabel.getStyleClass().add("standard-font");
            this.timeLabel.setText(this.testData.getTimeText());
            this.mainContentHeader.getChildren().addAll(space,this.timeLabel);
        }

        this.handleNextQuestion(0);
    }

    private void handleNextQuestion(int nextQuestionIndex){

        if(nextQuestionIndex > this.testData.getQuestionsUsed().size() - 1){
            handleEnd();
            return;
        }

        Question question = this.testData.getQuestionsUsed().get(nextQuestionIndex);
        Label questionLabel = new Label(question.getDescription());

        this.mainContentHeaderLabel.setText("Question " + (nextQuestionIndex + 1) );
        questionLabel.getStyleClass().add("standard-font");
        this.centralContent.getChildren().setAll(questionLabel);

        this.addAnswerFields(
            question, 
            nextQuestionIndex,
            this.testData.isTimed() ?
            new AnimationTimer() {
                long lastRun = 0;
                int currentSeconds = testData.getSeconds();

                @Override
                public void handle(long now){
                    if (this.lastRun == 0) {this.lastRun = now;}
                    if (((now - this.lastRun) / 1e9) >= 1){
                        System.out.println(currentSeconds);
                        this.lastRun = now;
                        timeLabel.setText(
                            Converter.minuteTextForm(--this.currentSeconds)
                        );
                    }
                    if (this.currentSeconds == 0){
                        handleQuestionResult(question, false, nextQuestionIndex, this);
                    }
                }
            } : null
        
        );

    }

    private void handleQuestionResult(Question question, boolean correct, int currentIndex, AnimationTimer timer){
        if(timer != null) timer.stop();
        if(correct) this.testData.incrementScore();
        final int currentScore = this.testData.getScore();
        final double correctProgress = (double)currentScore / this.testData.getQuestionsUsed().size();
        final double incorrectProgress = (double)(currentIndex + 1 - currentScore) 
        / this.testData.getQuestionsUsed().size() + correctProgress;
        final double transitionDuration = 0.5;
        Transitions.timelineTransition(this.correctProgressBar.progressProperty(), correctProgress, transitionDuration);
        Transitions.timelineTransition(this.incorrectProgressBar.progressProperty(), incorrectProgress, transitionDuration);
        correctProgressBar.setProgress(correctProgress);
        incorrectProgressBar.setProgress(incorrectProgress);
        if(!this.testData.isContinuous()){
            this.showQuestionResult(question, correct, currentIndex);
        } else {
            if (this.testData.isTimed()) this.timeLabel.setText(this.testData.getTimeText());
            this.handleNextQuestion(currentIndex + 1);
        }
    }

    private void showQuestionResult(Question question, boolean correct, int currentIndex){
        Text correctText = new Text(correct ? "You are correct!\n" : "Sadly, you missed it!\n");
        correctText.getStyleClass().add("standard-font");
        correctText.setFill(Color.WHITE);

        List<Choice> allCorrectAnswer = new ArrayList<>();
        for(Choice choice: question.getChoices()){
            if(choice.isAnswer()){
                allCorrectAnswer.add(choice);
            }
        }

        Text correctAnswersHeaderText = new Text(allCorrectAnswer.size() < 2 ?
            "The correct answer is:\n" :
            "The correct answers are:\n" 
        );
        correctAnswersHeaderText.getStyleClass().add("standard-font");
        correctAnswersHeaderText.setFill(Color.WHITE);
        correctText.wrappingWidthProperty().bind(this.mainContentPane.widthProperty());
        correctAnswersHeaderText.wrappingWidthProperty().bind(this.mainContentPane.widthProperty());

        TextFlow questionResultContainer = new TextFlow(correctText, correctAnswersHeaderText);
        for(Choice correctChoice : allCorrectAnswer){
            Text correctAnswerText = new Text(correctChoice.getDescription() + "\n");
            correctAnswerText.wrappingWidthProperty().bind(this.mainContentPane.widthProperty());
            correctAnswerText.getStyleClass().add("standard-font");
            correctAnswerText.setFill(Color.WHITE);
            questionResultContainer.getChildren().add(correctAnswerText);
        }
        questionResultContainer.setTextAlignment(TextAlignment.CENTER);
        questionResultContainer.getStyleClass().add("standard-font");
        this.centralContent.getChildren().setAll(questionResultContainer);

        Button nextButton = new Button("Next Question");
        nextButton.getStyleClass().addAll("standard-font","component-standard-bg","border-radius-15");
        nextButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setOnAction(e -> {
            if(this.testData.isTimed()) this.timeLabel.setText(this.testData.getTimeText());
            this.handleNextQuestion(currentIndex + 1);
        });
        this.mainContentPane.setBottom(nextButton);

    }

    private void handleEnd(){
        Transitions.timelineTransition(this.mainContentHeaderLabel.textProperty(), "Test Results", 0.5);
        if(this.testData.isTimed()) this.timeLabel.setText(null);
        Label scoreLabel = new Label("Final Score: " + this.testData.getScore());
        scoreLabel.getStyleClass().add("standard-font");
        this.centralContent.getChildren().setAll(scoreLabel);
        this.mainContentPane.setBottom(null);
    }

    private void addAnswerFields(Question question, int currentIndex, AnimationTimer timer){
        switch(this.testData.getType()){
            case TestType.MULTIPLE_CHOICE -> {

            }
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
                            currentIndex,
                            timer
                        );
                    }
                });

                VBox answerFieldContainer = new VBox(24, trueFalseContainer, submitButton);
                answerFieldContainer.setFillWidth(true);
                this.mainContentPane.setBottom(answerFieldContainer);
                Transitions.standardFadeTransition(answerFieldContainer);
            }
        }

        if(this.testData.isTimed()) timer.start();
    }

    @FXML
    public void initialize(){
        this.incorrectProgressBar.setProgress(0.0001);
        this.correctProgressBar.setProgress(0.0001);
    }
    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.testNavigator.show(TestPageURL.SETS, this.testData.getType());
    }
}