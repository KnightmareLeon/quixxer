package io.github.knightmareleon.features.test.components.pages;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.components.TestConfigReceiver;
import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.constants.TestPageURL;
import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.constants.StandardStyleClass;
import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.TestConfig;
import io.github.knightmareleon.shared.models.TestSession;
import io.github.knightmareleon.shared.ui.controls.StandardAlert;
import io.github.knightmareleon.shared.utils.Converter;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
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

public class TestPlayerController implements TestPage, TestConfigReceiver{

    private TestNavigator testNavigator;
    private TestConfig testConfig;
    private final List<TestSession> testSessions = new ArrayList<>();
    private int currentSessionIndex = 0;

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
    public void receiveTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
        this.testSessions.add(new TestSession(
            this.testConfig.getQuestions(),
            this.testConfig.isShuffled(),
            this.testConfig.getMaxTotalQuestions())
        );
        this.initWithTestConfig();
    }

    private void initWithTestConfig(){
        this.testPlayerTitle.setText(this.testConfig.getStudySetTitle() + " Test");

        if(this.testConfig.isTimed()){
            Region space = new Region();
            space.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(space, Priority.ALWAYS);

            this.timeLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
            this.timeLabel.setText(this.testConfig.getTimeSettingText());
            this.mainContentHeader.getChildren().addAll(space,this.timeLabel);
        }

        this.handleNextQuestion();
    }

    private TestSession currentTestSession(){return this.testSessions.get(this.currentSessionIndex);}

    private void handleNextQuestion(){

        if(this.currentTestSession().getCurrentIndex() >= this.currentTestSession().getTotalQuestions()){
            handleEnd();return;
        }

        Question question = this.currentTestSession().getCurrentQuestion();
        Text questionLabel = new Text(question.getDescription());
        questionLabel.wrappingWidthProperty().bind(this.mainContentHeader.widthProperty().add(-56));
        questionLabel.setFill(Color.WHITE);
        questionLabel.setTextAlignment(TextAlignment.CENTER);

        this.mainContentHeaderLabel.setText("Question " + (this.currentTestSession().getCurrentIndex() + 1) );
        questionLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

        this.centralContent.getChildren().setAll(questionLabel);

        this.addAnswerFields(
            question,
            this.testConfig.isTimed() ?
            new AnimationTimer() {
                long lastRun = 0;
                int currentSeconds = testConfig.getTimeSettingSeconds();

                @Override
                public void handle(long now){
                    if (this.lastRun == 0) {this.lastRun = now;}
                    if (((now - this.lastRun) / 1e9) >= 1){
                        this.lastRun = now;
                        timeLabel.setText(
                            Converter.minuteTextForm(--this.currentSeconds)
                        );
                    }
                    if (this.currentSeconds == 0){
                        handleQuestionResult(question, false, this);
                    }
                }
            } : null
        
        );

    }

    private void handleQuestionResult(Question question, boolean correct, AnimationTimer timer){
        if(timer != null) timer.stop();

        if(correct) this.currentTestSession().incrementScore();
        final int currentScore = this.currentTestSession().getScore();

        final double correctProgress = (double)currentScore / this.testConfig.getMaxTotalQuestions();
        final double incorrectProgress = (double)(this.currentTestSession().getCurrentIndex() + 1 - currentScore) 
        / this.testConfig.getMaxTotalQuestions() + correctProgress;

        final double transitionDuration = 0.5;
        Transitions.timelineTransition(this.correctProgressBar.progressProperty(), correctProgress, transitionDuration);
        Transitions.timelineTransition(this.incorrectProgressBar.progressProperty(), incorrectProgress, transitionDuration);

        correctProgressBar.setProgress(correctProgress);
        incorrectProgressBar.setProgress(incorrectProgress);

        if(!this.testConfig.isContinuous()){
            this.showQuestionResult(question, correct);
        } else {
            if (this.testConfig.isTimed()) this.timeLabel.setText(this.testConfig.getTimeSettingText());
            this.currentTestSession().getAndIncrementIndex();
            this.handleNextQuestion();
        }
    }

    private void showQuestionResult(Question question, boolean correct){
        Text correctText = new Text(correct ? "You are correct!\n" : "Sadly, you missed it!\n");
        correctText.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
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
        correctAnswersHeaderText.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
        correctAnswersHeaderText.setFill(Color.WHITE);
        correctText.wrappingWidthProperty().bind(this.mainContentPane.widthProperty());
        correctAnswersHeaderText.wrappingWidthProperty().bind(this.mainContentPane.widthProperty());

        TextFlow questionResultContainer = new TextFlow(correctText, correctAnswersHeaderText);
        for(Choice correctChoice : allCorrectAnswer){
            Text correctAnswerText = new Text(correctChoice.getDescription() + "\n");
            correctAnswerText.wrappingWidthProperty().bind(this.mainContentPane.widthProperty());
            correctAnswerText.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
            correctAnswerText.setFill(Color.WHITE);
            questionResultContainer.getChildren().add(correctAnswerText);
        }
        questionResultContainer.setTextAlignment(TextAlignment.CENTER);
        questionResultContainer.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
        this.centralContent.getChildren().setAll(questionResultContainer);

        Button nextButton = new Button("Next Question");
        nextButton.getStyleClass().addAll(
            StandardStyleClass.STANDARD_FONT,
            StandardStyleClass.COMPONENT_BG,
            StandardStyleClass.BORDER_RADIUS_15);
        nextButton.setMaxWidth(Double.MAX_VALUE);
        nextButton.setOnAction(e -> {
            if(this.testConfig.isTimed()) this.timeLabel.setText(this.testConfig.getTimeSettingText());
            this.currentTestSession().getAndIncrementIndex();
            this.handleNextQuestion();
        });
        this.mainContentPane.setBottom(nextButton);

    }

    private void handleEnd(){
        Transitions.timelineTransition(this.mainContentHeaderLabel.textProperty(), "Test Results", 0.5);
        if(this.testConfig.isTimed()) this.timeLabel.setText(null);

        Label scoreLabel = new Label("Final Score: " + this.currentTestSession().getScore());
        scoreLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);

        this.centralContent.getChildren().setAll(scoreLabel);

        Button newSessionButton = new Button("Attempt Again");
        Button reviewButton = new Button("Review");

        newSessionButton.getStyleClass().addAll(
            StandardStyleClass.STANDARD_FONT,
            StandardStyleClass.COMPONENT_BG,
            StandardStyleClass.BORDER_RADIUS_15
        );
        reviewButton.getStyleClass().addAll(
            StandardStyleClass.STANDARD_FONT,
            StandardStyleClass.COMPONENT_BG,
            StandardStyleClass.BORDER_RADIUS_15
        );

        newSessionButton.setOnAction(e -> {
            this.correctProgressBar.setProgress(0);
            this.incorrectProgressBar.setProgress(0);
            this.createNewSession();
            this.handleNextQuestion();
        });

        newSessionButton.setMaxWidth(Double.MAX_VALUE);
        reviewButton.setMaxWidth(Double.MAX_VALUE);

        VBox endBottomContainer = new VBox(24, newSessionButton, reviewButton);
        endBottomContainer.setFillWidth(true);
        this.mainContentPane.setBottom(endBottomContainer);
    }

    private void addAnswerFields(Question question, AnimationTimer timer){
        VBox answerFieldContainer = new VBox(24);
        answerFieldContainer.setFillWidth(true);
        switch(this.testConfig.getType()){
            case TestType.MULTIPLE_CHOICE -> {
                List<Choice> choices = question.getChoices();
                if(this.testConfig.isRandomized()) java.util.Collections.shuffle(choices);
                int size = question.getChoices().size();
                ToggleButton[] choiceToggleButtons = new ToggleButton[size];
                for(int i = 0; i < size; i++){
                    ToggleButton newToggleButton = new ToggleButton(
                        question.getChoices().get(i).getDescription()
                    );
                    newToggleButton.getStyleClass().addAll(
                        StandardStyleClass.STANDARD_FONT,
                        StandardStyleClass.COMPONENT_BG,
                        StandardStyleClass.BORDER_RADIUS_15
                    );
                    newToggleButton.setMaxWidth(Double.MAX_VALUE);
                    choiceToggleButtons[i] = newToggleButton;
                    answerFieldContainer.getChildren().add(newToggleButton);
                }

                Button submitButton = new Button("Submit");
                submitButton.getStyleClass().addAll(
                    StandardStyleClass.STANDARD_FONT,
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.BORDER_RADIUS_15
                );
                submitButton.setStyle("-fx-font-weight: bolder");
                submitButton.setMaxWidth(Double.MAX_VALUE);
                submitButton.setOnAction(e -> {
                    int totalNotSelected = 0;
                    for(int i = 0; i < size; i++){
                        if(!question.getChoices().get(i).isAnswer() && 
                            choiceToggleButtons[i].isSelected()){
                                this.handleQuestionResult(question, false, timer);
                                return;
                        }
                        if(!choiceToggleButtons[i].isSelected()) totalNotSelected++;
                    }

                    if(totalNotSelected == size) {
                        this.handleQuestionResult(question, false, timer);
                        return;
                    }

                    this.handleQuestionResult(question, true, timer);
                });

                answerFieldContainer.getChildren().add(submitButton);
            }
            case TestType.FLASHCARD -> {

                Button submitButton = new Button("Submit");
                submitButton.getStyleClass().addAll(
                    StandardStyleClass.STANDARD_FONT,
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.BORDER_RADIUS_15
                );
                submitButton.setStyle("-fx-font-weight: bolder");
                submitButton.setMaxWidth(Double.MAX_VALUE);

                if(!this.testConfig.isInTextInputMode()){
                    ToggleButton markRightButton = new ToggleButton("Mark Right");
                    markRightButton.getStyleClass().addAll(
                        StandardStyleClass.STANDARD_FONT,
                        StandardStyleClass.COMPONENT_BG,
                        StandardStyleClass.BORDER_RADIUS_15
                    );
                    markRightButton.setMaxWidth(Double.MAX_VALUE);

                    submitButton.setOnAction(e -> {
                    this.handleQuestionResult(
                            question,
                            markRightButton.isSelected(),
                            timer
                        );
                    });

                    answerFieldContainer.getChildren().add(markRightButton);
                } else {
                    TextField inputTextField = new TextField();
                    inputTextField.getStyleClass().add(
                        StandardStyleClass.STANDARD_FONT
                    );
                    inputTextField.setMaxWidth(Double.MAX_VALUE);
                    inputTextField.setStyle(
                        "-fx-text-fill: black !important; " + 
                        "-fx-text-base-color: black !important"
                    );
                    submitButton.setOnAction(e -> {
                        String answer = inputTextField.getText();
                        String correctAnswer = question.getChoices().get(0).getDescription();
                        if(this.testConfig.ignoreCases()) {
                            answer = answer.toLowerCase();
                            correctAnswer = correctAnswer.toLowerCase();
                        }
                        if(this.testConfig.ignorePunctuation()){
                            answer = answer.replaceAll("\\p{Punct}", "");
                            correctAnswer = correctAnswer.replaceAll("\\p{Punct}", "");
                        }
                        if(this.testConfig.ignoreSpaces()){
                            answer = answer.replaceAll("\\s+", "");
                            correctAnswer = correctAnswer.replaceAll("\\s+", "");
                        }
                        handleQuestionResult(
                            question,
                            answer.equals(correctAnswer),
                            timer
                        );
                    });

                    answerFieldContainer.getChildren().add(inputTextField);
                }


                answerFieldContainer.getChildren().add(submitButton);
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

                trueButton.getStyleClass().addAll(
                    StandardStyleClass.STANDARD_FONT,
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.BORDER_RADIUS_15,
                    StandardStyleClass.WHITE_BORDER);
                falseButton.getStyleClass().addAll(
                    StandardStyleClass.STANDARD_FONT,
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.BORDER_RADIUS_15,
                    StandardStyleClass.WHITE_BORDER);
                trueButton.setMaxWidth(Double.MAX_VALUE);
                falseButton.setMaxWidth(Double.MAX_VALUE);

                trueFalseContainer.getChildren().addAll(trueButton,falseButton);
                trueFalseContainer.setFillHeight(true);
                trueFalseContainer.setSpacing(24);

                Button submitButton = new Button("Submit");
                submitButton.getStyleClass().addAll(
                    StandardStyleClass.STANDARD_FONT,
                    StandardStyleClass.COMPONENT_BG,
                    StandardStyleClass.BORDER_RADIUS_15
                );
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
                            timer
                        );
                    }
                });

                answerFieldContainer.getChildren().setAll(trueFalseContainer,submitButton);
            }
            default -> {
                System.out.println("Test Type not supported.");
            }
        }

        this.mainContentPane.setBottom(answerFieldContainer);
                Transitions.standardFadeTransition(answerFieldContainer);

        if(this.testConfig.isTimed()) timer.start();
    }

    private void createNewSession(){
        this.currentSessionIndex++;
        this.testSessions.add(new TestSession(
            this.testConfig.getQuestions(),
            this.testConfig.isShuffled(),
            this.testConfig.getMaxTotalQuestions())
        );
    }

    @FXML
    public void initialize(){
        this.incorrectProgressBar.setProgress(0.0001);
        this.correctProgressBar.setProgress(0.0001);
    }
    @FXML
    @SuppressWarnings("unused")
    private void onBackPageClicked(){
        this.testNavigator.show(TestPageURL.SETS, this.testConfig.getType());
    }
}