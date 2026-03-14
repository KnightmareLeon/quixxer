package io.github.knightmareleon.features.sets;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.features.sets.components.constants.SetsConstants;
import io.github.knightmareleon.shared.constants.QuestionType;
import io.github.knightmareleon.shared.daos.SetsDao;
import io.github.knightmareleon.shared.exceptions.DataAccessException;
import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.Result;

public class SetsService {
    
    private final SetsDao setsDao;
    private final int MAX_SET_TOTAL_PER_PAGE = 10;

    private int totalPages;

    public SetsService(SetsDao setsDao){
        this.setsDao = setsDao;
        try {
            this.totalPages = (int) Math.ceil((double)(this.setsDao.totalRows()) / (double)(this.MAX_SET_TOTAL_PER_PAGE));
        } catch (DataAccessException e) {
            
        }
    }
    
    public Result<StudySet> saveStudySet(StudySet studySet){
        List<String> errorMessages = new ArrayList<>();
        if(studySet.getTitle() == null || studySet.getTitle().isBlank()){
            errorMessages.add(SetsConstants.MISSING_TITLE_ERROR);
        }

        if(studySet.getSubject() == null || studySet.getSubject().isBlank()){
            errorMessages.add(SetsConstants.MISSING_SUBJECT_ERROR);
        }

        if(studySet.getQuestions().isEmpty()){
            errorMessages.add(SetsConstants.MISSING_QUESTIONS_ERROR);
        }

        if(setsDao.exists(studySet.getTitle(), studySet.getSubject())){
            errorMessages.add(SetsConstants.DUPLICATE_STUDY_SET_ERROR);
        }

        for(int i = 0; i < studySet.getQuestions().size(); i++){
            Question question = studySet.getQuestions().get(i);
            if(question.getDescription().isBlank()){
                errorMessages.add("" + i);
            }

            else if( question.getType() == QuestionType.IDENTIFICATION ||
                question.getType() == QuestionType.ENUMERATION
            ){
                for(Choice choice : question.getChoices()){
                    if(choice.getDescription() == null || choice.getDescription().isBlank()){
                        errorMessages.add("" + i);
                        break;
                    }
                }
            }
        }

        if(errorMessages.isEmpty()){
            try {
                this.setsDao.save(studySet);
            } catch (DataAccessException e) {
                errorMessages.add("Database error.");
            }
        }

        return errorMessages.isEmpty() ? Result.success(studySet) : Result.error(errorMessages);
    }

    public Result<List<StudySet>> getStudySets(int page){ 
        
        if(page < 1) page = 1;

        try {
            if(page > this.totalPages) page = this.totalPages;
            List<StudySet> studySets = this.setsDao.list(
                this.MAX_SET_TOTAL_PER_PAGE, 
                (page - 1) * this.MAX_SET_TOTAL_PER_PAGE
            );
            return Result.success(studySets);
        } catch (DataAccessException e) {
            return Result.error("Error fetching study sets.");
        }

    }

    public Result<String> deleteStudyResult(int studySetID){
        try {
            this.setsDao.delete(studySetID);
            return Result.success("Delete successful for study set.");
        } catch (DataAccessException e) {
            return Result.error("Failed to delete study set.");
        }
    }
}