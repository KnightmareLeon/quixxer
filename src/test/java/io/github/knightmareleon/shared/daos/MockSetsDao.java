package io.github.knightmareleon.shared.daos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.knightmareleon.features.test.components.constants.TestType;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;

public class MockSetsDao implements SetsDao {

    private final List<StudySet> studySets = new ArrayList<>();

    public MockSetsDao(){
        this.studySets.add(
            new StudySet(
                1,
                "Study Set 1",
                "Computer Science",
                "default",
                5,
                List.of(new Question(
                    1,
                    "True or False. Ada Lovelace is the first programmer.", 
                    QuestionType.TRUE_OR_FALSE, 
                    List.of(
                        new Choice(
                            "True",
                        true),
                        new Choice(
                            "False",
                            false
                        )
                ))),
                Instant.parse("2001-09-15T06:00:00Z"),
                Instant.parse("2001-09-15T12:00:00Z")
            )
        );
    }

    @Override
    public void save(StudySet studySet) {
        this.studySets.add(studySet);
    }

    @Override
    public void updateDetails(StudySet studySet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exists(String title, String subject) {
        for(StudySet studySet: this.studySets){
            if(studySet.getTitle().equals(title) && studySet.getSubject().equals(subject)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int totalRows() {
        return this.studySets.size();
    }

    @Override
    public int totalRowsByTest(TestType testType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StudySet> list(int limit, int offset) {
        List<StudySet> toBeReturned = this.studySets.stream()
            .skip(offset)
            .limit(limit)
            .collect(Collectors.toList());
        return toBeReturned;
    }

    @Override
    public List<StudySet> listByTest(int limit, int offset, TestType testType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(int studySetID) {
        for(int i = 0; i < this.studySets.size(); i++){
            if(this.studySets.get(i).getId() == studySetID){
                this.studySets.remove(this.studySets.get(i));
                break;
            }
        }
    }
    
}
