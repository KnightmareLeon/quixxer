package io.github.knightmareleon.features.test.components.constants;

public enum TestType {
    MULTIPLE_CHOICE("Multiple Choice"),
    FLASHCARD("Flashcard"),
    MATCHING_TYPE("Matching Type"),
    ENUMERATION("Enumeration"),
    TRUE_OR_FALSE("True or False"),
    COMBINED("Combined");

    private final String name;

    TestType(String name) {
        this.name = name;
    }

    public String getName() {return this.name;}
}
