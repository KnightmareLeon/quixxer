package io.github.knightmareleon.shared.models;

import java.util.List;

import io.github.knightmareleon.shared.constants.QuestionType;

public class Question{

    private final int id;
    private String description; 
    private QuestionType type; 
    private final List<String> choices; 
    private final List<Integer> answerIndices;

    public Question(int id, 
        String description,
        QuestionType type,
        List<String> choices,
        List<Integer> answerIndices){
        
        this.id = id;
        this.description = description;
        this.type = type;
        this.choices = choices;
        this.answerIndices = answerIndices;

    }

    public Question(String description,
        QuestionType type,
        List<String> choices,
        List<Integer> answerIndices){
        
        this.id = 0;
        this.description = description;
        this.type = type;
        this.choices = choices;
        this.answerIndices = answerIndices;
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

    public List<String> getChoices(){
        return this.choices;
    }

    public List<Integer> getAnswerIndices(){
        return this.answerIndices;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setType(QuestionType type){
        this.type = type;
    }
}