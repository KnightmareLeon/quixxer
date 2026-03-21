package io.github.knightmareleon.features.sets.constants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SetsConstants {
    public static final String MISSING_TITLE_ERROR = "Missing Title";
    public static final String MISSING_SUBJECT_ERROR = "Missing Subject";
    public static final String MISSING_QUESTIONS_ERROR = "Missing Questions";
    public static final String DUPLICATE_STUDY_SET_ERROR = "Duplicate Study Set";
    public static final String DATABASE_ERROR = "Database Error";

    public static final ObservableList<String> SUBJECTS = FXCollections.observableArrayList(
        "Computer Science",
        "Physics",
        "Biology",
        "Chemistry",
        "Geology",
        "Astrology",
        "Religion",
        "History",
        "Language",
        "Mathematics",
        "Other"
    );

    private SetsConstants(){}
}
