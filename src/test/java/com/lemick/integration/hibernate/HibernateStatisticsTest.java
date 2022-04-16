package com.lemick.integration.hibernate;

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
        assertEquals(0, model.getSelectStatementCount(), "the count is initialized to 0");
        assertEquals(0, model.getInsertStatementCount(), "the count is initialized to 0");
        assertEquals(0, model.getUpdateStatementCount(), "the count is initialized to 0");
        assertEquals(0, model.getDeleteStatementCount(), "the count is initialized to 0");
    }

    @Test
    public void _reset() {
        model.notifySelectStatement();
        model.notifyInsertStatement();
        model.notifyUpdateStatement();
        model.notifyDeleteStatement();

        model.resetStatistics();

        assertEquals(0, model.getSelectStatementCount(), "the count is reset to 0");
        assertEquals(0, model.getInsertStatementCount(), "the count is reset to 0");
        assertEquals(0, model.getUpdateStatementCount(), "the count is reset to 0");
        assertEquals(0, model.getDeleteStatementCount(), "the count is reset to 0");
    }

    @Test
    public void _notify_increments() {
        model.notifySelectStatement();
        model.notifyInsertStatement();
        model.notifyUpdateStatement();
        model.notifyDeleteStatement();

        assertEquals(1, model.getSelectStatementCount(), "the count is incremented");
        assertEquals(1, model.getInsertStatementCount(), "the count is incremented");
        assertEquals(1, model.getUpdateStatementCount(), "the count is incremented");
        assertEquals(1, model.getDeleteStatementCount(), "the count is incremented");
    }
}
