package com.mickaelb.integration.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HibernateStatisticsTest {

    @InjectMocks
    HibernateStatistics model;

    @Test
    public void _default_state() {
        assertEquals(0, model.getSelectStatements().size(), "the count is initialized to 0");
        assertEquals(0, model.getInsertStatements().size(), "the count is initialized to 0");
        assertEquals(0, model.getUpdateStatements().size(), "the count is initialized to 0");
        assertEquals(0, model.getDeleteStatements().size(), "the count is initialized to 0");
    }

    @Test
    public void _reset() {
        model.notifySelectStatement("SELECT * FROM Post");
        model.notifyInsertStatement("INSERT INTO Post VALUES (X)");
        model.notifyUpdateStatement("UPDATE Post SET X=X");
        model.notifyDeleteStatement("DELETE * FROM Post");

        model.resetStatistics();

        assertEquals(0, model.getSelectStatements().size(), "the count is reset to 0");
        assertEquals(0, model.getInsertStatements().size(), "the count is reset to 0");
        assertEquals(0, model.getUpdateStatements().size(), "the count is reset to 0");
        assertEquals(0, model.getDeleteStatements().size(), "the count is reset to 0");
    }

    @Test
    public void _notify_increments() {
        model.notifySelectStatement("SELECT * FROM Post");
        model.notifyInsertStatement("INSERT INTO Post VALUES (X)");
        model.notifyUpdateStatement("UPDATE Post SET X=X");
        model.notifyDeleteStatement("DELETE * FROM Post");

        assertEquals(1, model.getSelectStatements().size(),  "the count is incremented");
        assertEquals(1, model.getInsertStatements().size(),  "the count is incremented");
        assertEquals(1, model.getUpdateStatements().size(),  "the count is incremented");
        assertEquals(1, model.getDeleteStatements().size(),  "the count is incremented");
    }
}
