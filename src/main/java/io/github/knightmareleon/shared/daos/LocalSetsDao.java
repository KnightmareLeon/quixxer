package io.github.knightmareleon.shared.daos;

import java.sql.Connection;

import io.github.knightmareleon.shared.models.StudySet;

public class LocalSetsDao implements SetsDao{

    private final Connection connection;

    public LocalSetsDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(StudySet studySet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exists(String title, String subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
