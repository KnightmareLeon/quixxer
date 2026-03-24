package io.github.knightmareleon.shared.models;

import java.util.List;

public class TestSession {
    
    private final List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;

    private final Choice[][] answers;

    public TestSession(List<Question> questions, boolean shuffled, int totalMaxQuestions){
        if(shuffled) java.util.Collections.shuffle(questions);
        this.questions = questions.subList(0, totalMaxQuestions);
        this.answers = new Choice[this.questions.size()][];
    }

    public Question getQuestion(int index){return this.questions.get(index);}

    public Question getCurrentQuestion(){return this.questions.get(this.currentIndex);}

    public int getTotalQuestions(){return this.questions.size();}

    public int getCurrentIndex(){return this.currentIndex;}

    public int getScore(){return this.score;}

    public Choice[][] getAnswers(){
        Choice[][] answersCopy = new Choice[this.answers.length][];
        for(int i = 0; i < this.answers.length; i++){
            answersCopy[i] = new Choice[answersCopy[i].length];
            for(int j = 0; j < this.answers[i].length; j++){
                answersCopy[i][j] = new Choice(
                    this.answers[i][j].getDescription(),
                    this.answers[i][j].isAnswer()
                );
            }
        }
        return answersCopy;
    }

    public int getAndIncrementIndex(){return this.currentIndex++;}

    public void incrementScore(){this.score++;}

    public void saveAnswers(Choice[] answers){
        this.answers[this.currentIndex] = answers;
    }

}
