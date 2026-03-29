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

    private final boolean randomized;
    private final boolean inTextInputMode;
    private final boolean ignoreCases;
    private final boolean ignorePunctuation;
    private final boolean ignoreSpaces;

    private TestConfig(
        TestType type,
        StudySet studySet, 
        int maxTotalQuestions,
        boolean timed,
        TimeSetting timeSetting,
        boolean shuffled,
        boolean continuous,
        boolean randomized,
        boolean inTextInputMode,
        boolean ignoreCases,
        boolean ignorePunctuation,
        boolean ignoreSpaces){
            this.type = type;
            this.studySet = studySet;
            this.maxTotalQuestions = maxTotalQuestions;
            this.timed = timed;
            this.timeSetting = timeSetting;
            this.continuous = continuous;
            this.shuffled = shuffled;
            this.randomized = randomized;
            this.inTextInputMode = inTextInputMode;
            this.ignoreCases = ignoreCases;
            this.ignorePunctuation = ignorePunctuation;
            this.ignoreSpaces = ignoreSpaces;
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

    public boolean isRandomized(){return this.randomized;}

    public boolean isInTextInputMode(){return this.inTextInputMode;}

    public boolean ignoreCases(){return this.ignoreCases;}

    public boolean ignorePunctuation(){return this.ignorePunctuation;}

    public boolean ignoreSpaces(){return this.ignoreSpaces;}

    public static class Builder{
        private final TestType type;
        private final StudySet studySet;
        private final int maxTotalQuestions;

        private final boolean timed;
        private final TimeSetting timeSetting;
        private final boolean continuous;
        private final boolean shuffled;

        private boolean randomized = false;
        private boolean inTextInputMode = false;
        private boolean ignoreCases = false;
        private boolean ignorePunctuation = false;
        private boolean ignoreSpaces = false;

        public Builder(
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

        public void setRandomized(boolean value){
            this.randomized = value;
        }

        public void setIfTextInput(boolean value){
            this.inTextInputMode = value;
        }

        public void setIgnoreCases(boolean value){
            this.ignoreCases = value;
        }

        public void setIgnorePunctuation(boolean value){
            this.ignorePunctuation = value;
        }

        public void setIgnoreSpaces(boolean value){
            this.ignoreSpaces = value;
        }

        public TestConfig build(){
            return new TestConfig(
                this.type, 
                this.studySet, 
                this.maxTotalQuestions,
                this.timed, 
                this.timeSetting, 
                this.shuffled, 
                this.continuous,
                this.randomized,
                this.inTextInputMode,
                this.ignoreCases,
                this.ignorePunctuation,
                this.ignoreSpaces);
        }
    }
}
