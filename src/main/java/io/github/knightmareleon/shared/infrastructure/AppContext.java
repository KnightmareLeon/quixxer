package io.github.knightmareleon.shared.infrastructure;

import java.sql.Connection;

import io.github.knightmareleon.features.dashboard.DashboardService;
import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.features.test.TestService;
import io.github.knightmareleon.shared.daos.LocalSetsDao;
import io.github.knightmareleon.shared.daos.SetsDao;

public class AppContext {
    
    private final DashboardService dbService;
    private final SetsService setsService;
    private final TestService testService;

    public AppContext(Connection connection) {
        SetsDao dao = new LocalSetsDao(connection);
        this.testService = new TestService(dao);
        this.setsService = new SetsService(dao);
        this.dbService = new DashboardService(dao);
    }

    public SetsService getSetService() {
        return this.setsService;
    }

    public TestService getTestService() {
        return this.testService;
    }

    public DashboardService getDBService(){
        return this.dbService;
    }

}
