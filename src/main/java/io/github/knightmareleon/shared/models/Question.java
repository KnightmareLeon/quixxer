package io.github.knightmareleon.shared.models;

import java.util.List;

import io.github.knightmareleon.shared.constants.QuestionType;

public class Question{

    private final int id;
    private String description; 
    private QuestionType type; 
    private final List<Choice> choices;

    public Question(int id, 
        String description,
        QuestionType type,
        List<Choice> choices){
        
        this.id = id;
        this.description = description;
        this.type = type;
        this.choices = choices;

    }

    public Question(String description,
        QuestionType type,
        List<Choice> choices){
        
        this.id = 0;
        this.description = description;
        this.type = type;
        this.choices = choices;
    }

    public int getId(){
        return this.id;
    }

    public String getDescription(){
        return this.description;
    }

    public QuestionType getType(){
        return this.type;
    }

    public List<Choice> getChoices(){
        return this.choices;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setType(QuestionType type){
        this.type = type;
    }

    @Override
    public String toString(){
        return String.format(
            " Question: %s | Type: %s |" + 
            " Choices: %s ", 
            this.description,
            this.type,
            this.choices);
    }
}