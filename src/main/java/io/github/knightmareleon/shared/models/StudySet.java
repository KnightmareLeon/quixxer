package io.github.knightmareleon.shared.models;

import java.util.List;

public record StudySet(
    String name, 
    String subject, 
    List<Question> questions) {

}
