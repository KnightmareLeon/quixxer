package io.github.knightmareleon.features.sets;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.daos.MockSetsDao;
import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;

public class SetsServiceTest {

    private SetsService setsService;

    @BeforeEach
    public void initialize(){
        this.setsService = new SetsService(
            new MockSetsDao()
        );
    }

    @Test
    public void shoudlGetTotalPages(){
        assertEquals(1, this.setsService.getTotalPages());
    }

    @Test
    public void shouldSaveSet(){
        StudySet studySet = new StudySet(
            "Dummy Set", 
            "History", 
            "default", 
            List.of(new Question(
                "Dummy Question", 
                QuestionType.IDENTIFICATION, 
                List.of(
                    new Choice("Wow", true)
                )
            ))
        );
        this.setsService.saveStudySet(studySet);
        assertEquals(studySet, this.setsService.getStudySets(1).getValue().get(1));
    }

    @Test
    public void shouldDeleteSet(){
        StudySet studySet1 = new StudySet(
            10,
            "Dummy Set 1", 
            "History", 
            "default", 
            0,
            List.of(new Question(
                "Dummy Question", 
                QuestionType.IDENTIFICATION, 
                List.of(
                    new Choice("Wow", true)
                )
            )),
            Instant.now(),
            Instant.now()
        );
        StudySet studySet2 = new StudySet(
            11,
            "Dummy Set 2", 
            "History", 
            "default", 
            0,
            List.of(new Question(
                "Dummy Question", 
                QuestionType.IDENTIFICATION, 
                List.of(
                    new Choice("Wow", true)
                )
            )),
            Instant.now(),
            Instant.now()
        );
        this.setsService.saveStudySet(studySet1);
        this.setsService.saveStudySet(studySet2);
        this.setsService.deleteStudySet(studySet1.getId());
        assertEquals(studySet2, this.setsService.getStudySets(1).getValue().get(1));
    }
}
