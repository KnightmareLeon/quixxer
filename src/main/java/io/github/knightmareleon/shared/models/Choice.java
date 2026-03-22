package io.github.knightmareleon.shared.models;

public class Choice {
    private final int id;
    private String description;
    private boolean isAnswer;

    public Choice(String description, boolean isAnswer){
        this(1, description, isAnswer);
    }

    public Choice(int id, String description, boolean isAnswer){
        this.id = 1;
        this.description = description;
        this.isAnswer = isAnswer;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setAnswer(boolean isAnswer){
        this.isAnswer = isAnswer;
    }

    public int getId(){
        return this.id;
    }

    public String getDescription(){
        return this.description;
    }

    public boolean isAnswer(){
        return this.isAnswer;
    }
}
