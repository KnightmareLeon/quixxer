package io.github.knightmareleon.shared.models;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.components.constants.TestType;

public class TestData {
    private final TestType type;
    private final StudySet studySet;

    private List<Question> questionsUsed;

    private final boolean timed;
    private final String timeCategory;
    private final boolean continuous;

    private int score = 0;

    public TestData(TestType type, StudySet studySet, int totalQuestionsUsed,
                    boolean timed, String timeCategory, boolean shuffled, boolean continuous){
        this.type = type;
        this.studySet = studySet;
        this.timed = timed;
        this.timeCategory = timeCategory;
        this.setQuestionsUsed(this.studySet.getQuestions(), shuffled, totalQuestionsUsed);
        this.continuous = continuous;
    }

    private void setQuestionsUsed(List<Question> questionsList, boolean shuffled, int totalToBeUsed){
        List<Question> questionsToBeUsed = new ArrayList<>(questionsList);
        if(shuffled) java.util.Collections.shuffle(questionsToBeUsed);
        this.questionsUsed = questionsToBeUsed.subList(0, totalToBeUsed);
    }

    public TestType getType(){
        return this.type;
    }

    public List<Question> getQuestionsUsed(){
        return this.questionsUsed;
    }

    public boolean isTimed(){
        return this.timed;
    }

    public String getTimeCategory(){
        return this.timeCategory;
    }

    public void incrementScore(){
        this.score++;
    }

    public boolean isContinuous(){
        return this.continuous;
    }

    public int getScore(){
        return this.score;
    }

    public String getStudySetTitle(){
        return this.studySet.getTitle();
    }
}
