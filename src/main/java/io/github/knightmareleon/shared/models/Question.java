package io.github.knightmareleon.shared.models;

import java.util.List;

public record Question(
    int id,
    String question, 
    String questionType, 
    List<String> choices, 
    List<Integer> answerIndices) {

}