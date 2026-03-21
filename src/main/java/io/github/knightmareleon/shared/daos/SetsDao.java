package io.github.knightmareleon.shared.daos;

import java.util.List;

import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.models.StudySet;

public interface SetsDao {
    void save(StudySet studySet);
    void updateDetails(StudySet studySet);
    boolean exists(String title, String subject);
    int totalRows();
    int totalRowsByTest(TestType testType);
    List<StudySet> list(int limit, int offset);
    List<StudySet> listByTest(int limit, int offset, TestType testType);
    void delete(int studySetID);

}