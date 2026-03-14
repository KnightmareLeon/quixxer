package io.github.knightmareleon.shared.models;

public class Choice {
    private String description;
    private boolean isAnswer;

    public Choice(String description, boolean isAnswer){
        this.description = description;
        this.isAnswer = isAnswer;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setAnswer(boolean isAnswer){
        this.isAnswer = isAnswer;
    }

    public String getDescription(){
        return this.description;
    }

    public boolean isAnswer(){
        return this.isAnswer;
    }
}
