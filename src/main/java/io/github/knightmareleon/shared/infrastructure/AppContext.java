package io.github.knightmareleon.shared.infrastructure;

import java.sql.Connection;

import io.github.knightmareleon.features.sets.SetsService;
import io.github.knightmareleon.shared.daos.LocalSetsDao;
import io.github.knightmareleon.shared.daos.SetsDao;

public class AppContext {
    private final SetsService setsService;

    public AppContext(Connection connection) {
        SetsDao dao = new LocalSetsDao(connection);
        this.setsService = new SetsService(dao);
    }

    public SetsService getStudySetService() {
        return this.setsService;
    }

}
