package io.github.knightmareleon.shared.daos;

import io.github.knightmareleon.shared.models.StudySet;

public interface SetsDao {
    void save(StudySet studySet);
    boolean exists(String title, String subject);
}