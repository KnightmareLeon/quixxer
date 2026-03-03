package io.github.knightmareleon.features.test;

import io.github.knightmareleon.shared.daos.SetsDao;
import io.github.knightmareleon.shared.exceptions.DataAccessException;

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
}
