package io.github.knightmareleon.features.sets;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.shared.daos.SetsDao;
import io.github.knightmareleon.shared.exceptions.DataAccessException;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.Result;

public class SetsService {
    
    private final SetsDao setsDao;

    public SetsService(SetsDao setsDao){
        this.setsDao = setsDao;
    }
    
    public Result<StudySet> saveStudySet(StudySet studySet){
        List<String> errorMessages = new ArrayList<>();
        if(studySet.getTitle() == null || studySet.getTitle().isBlank()){
            errorMessages.add("Title Missing");
        }

        if(studySet.getSubject() == null || studySet.getSubject().isBlank()){
            errorMessages.add("Subject Missing");
        }

        if(studySet.getQuestions().isEmpty()){
            errorMessages.add("Study set must have at least one question.");
        }

        for(int i = 0; i < studySet.getQuestions().size(); i++){
            Question question = studySet.getQuestions().get(i);
            if(question.question().isBlank()){
                errorMessages.add("" + i);
            }

            else if( question.questionType().equals("Identification") ||
                question.questionType().equals("Enumeration")
            ){
                for(String choice : question.choices()){
                    if(choice == null || choice.isBlank()){
                        errorMessages.add("" + i);
                        break;
                    }
                }
            }
        }

        studySet.setDateCreatedOn();

        if(errorMessages.isEmpty()){
            try {
                this.setsDao.save(studySet);
            } catch (DataAccessException e) {
                errorMessages.add("Database error.");
            }
        }

        return errorMessages.isEmpty() ? Result.success(studySet) : Result.error(errorMessages);
    }
}