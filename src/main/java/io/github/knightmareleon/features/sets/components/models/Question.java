package io.github.knightmareleon.features.sets.components.models;

import java.util.List;

public record Question(String question, String questionType, List<String> choices, List<Integer> answerIndices) {

}