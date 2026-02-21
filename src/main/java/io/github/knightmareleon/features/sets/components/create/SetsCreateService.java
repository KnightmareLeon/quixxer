package io.github.knightmareleon.features.sets.components.create;

import io.github.knightmareleon.features.sets.components.models.StudySet;

public class SetsCreateService {
    
    public void saveStudySet(StudySet studySet){
        System.out.println(studySet.name());
        System.out.println(studySet.subject());
        System.out.println(studySet.questions());
    }
}
