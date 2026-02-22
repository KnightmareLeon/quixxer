package io.github.knightmareleon.shared.models;

import java.util.List;

public record Question(String question, String questionType, List<String> choices, List<Integer> answerIndices) {

}