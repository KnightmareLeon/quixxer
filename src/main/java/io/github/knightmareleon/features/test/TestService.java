package io.github.knightmareleon.features.test;

import java.util.List;

import io.github.knightmareleon.shared.daos.SetsDao;
import io.github.knightmareleon.shared.exceptions.DataAccessException;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.Result;

public class TestService {
    private final SetsDao setsDao;
    private final int MAX_SET_TOTAL_PER_PAGE = 10;

    private int totalPages;

    public TestService(SetsDao setsDao){
        this.setsDao = setsDao;
        try {
            this.totalPages = (int) Math.ceil((double)(this.setsDao.totalRows()) / (double)(this.MAX_SET_TOTAL_PER_PAGE));
        } catch (DataAccessException e) {
            
        } 
    }

    public Result<List<StudySet>> getSetsWithTrueOrFalseOnly (int page){
        if(page < 1) page = 1;

        try {
            if(page > this.totalPages) page = this.totalPages;
            List<StudySet> sets = this.setsDao.listByTest(
                this.MAX_SET_TOTAL_PER_PAGE, 
                (page - 1) * this.MAX_SET_TOTAL_PER_PAGE, 
                TestType.TRUE_OR_FALSE);
            return Result.success(sets);
        } catch (DataAccessException e) {
            return Result.error("Error getting sets with only true or false questions.");
        }
    }
}
