package io.github.knightmareleon.features.test;

import java.util.List;

import io.github.knightmareleon.features.test.constants.TestType;
import io.github.knightmareleon.shared.daos.SetsDao;
import io.github.knightmareleon.shared.exceptions.DataAccessException;
import io.github.knightmareleon.shared.models.StudySet;
import io.github.knightmareleon.shared.utils.Result;

public class TestService {
    private final SetsDao setsDao;
    private final int MAX_SET_TOTAL_PER_PAGE = 10;


    public TestService(SetsDao setsDao){
        this.setsDao = setsDao;
    }

    public Result<List<StudySet>> getSetsByTestType (int page, TestType type){
        if(page < 1) page = 1;

        try {
            int totalPages = this.getTotalPagesByTest(type);
            if(page > totalPages) page = totalPages;
            List<StudySet> sets = this.setsDao.listByTest(
                this.MAX_SET_TOTAL_PER_PAGE, 
                (page - 1) * this.MAX_SET_TOTAL_PER_PAGE, 
                type);
            return Result.success(sets);
        } catch (DataAccessException e) {
            return Result.error("Error getting sets with only true or false questions.");
        }
    }

    public int getTotalPagesByTest(TestType testType){
        return (int) Math.ceil(
            (double)(this.setsDao.totalRowsByTest(testType) / 
            (double)(this.MAX_SET_TOTAL_PER_PAGE)
        ));
    }
}
