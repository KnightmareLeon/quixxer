package io.github.knightmareleon.features.dashboard;

import io.github.knightmareleon.shared.daos.SetsDao;
import io.github.knightmareleon.shared.exceptions.DataAccessException;

public class DashboardService {
    
    private final SetsDao setsDao;
    private int totalStudySets;

    public DashboardService(SetsDao setsDao){
        this.setsDao = setsDao;
        try {
            this.totalStudySets = this.setsDao.totalRows();
        } catch (DataAccessException e) {

        }
    }

    public int getTotalSets(){
        return this.totalStudySets;
    }
}
