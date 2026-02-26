package io.github.knightmareleon.shared.constants;

public enum QuestionType {
    TRUE_OR_FALSE("True or False", -1),
    IDENTIFICATION("Identification", 0),
    ENUMERATION("Enumeration", 1);

    private final String name;
    private final int code;

    QuestionType(String name, int code){
        this.name = name;
        this.code = code;
    }

    public String getName() {return this.name;}
    public int getCode() {return this.code;}
}
