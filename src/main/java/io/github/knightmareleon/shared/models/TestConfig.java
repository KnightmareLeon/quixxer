package io.github.knightmareleon.shared.models;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.constants.TimeSetting;

public class TestConfig {
    private final TestType type;
    private final StudySet studySet;
    private final int maxTotalQuestions;

    private final boolean timed;
    private final TimeSetting timeSetting;
    private final boolean continuous;
    private final boolean shuffled;

    public TestConfig(
        TestType type,
        StudySet studySet, 
        int maxTotalQuestions,
        boolean timed,
        TimeSetting timeSetting,
        boolean shuffled,
        boolean continuous){
            this.type = type;
            this.studySet = studySet;
            this.maxTotalQuestions = maxTotalQuestions;
            this.timed = timed;
            this.timeSetting = timeSetting;
            this.continuous = continuous;
            this.shuffled = shuffled;
    }

    public TestType getType(){return this.type;}

    public String getStudySetTitle(){return this.studySet.getTitle();}

    public List<Question> getQuestions(){return new ArrayList<>(this.studySet.getQuestions());}

    public int getMaxTotalQuestions(){return this.maxTotalQuestions;}

    public boolean isTimed(){return this.timed;}

    public String getTimeSettingText(){return this.timeSetting.text();}

    public int getTimeSettingSeconds(){return this.timeSetting.seconds();}

    public boolean isContinuous(){return this.continuous;}

    public boolean isShuffled(){return this.shuffled;}
}
