package com.lemick.integration.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HibernateStatementCountInspectorTest {

    @InjectMocks
    HibernateStatementCountInspector model;

    @Mock
    HibernateStatementParser statementParser;

    @Test
    public void _inspect() {
        String actual = model.inspect("SELECT * FROM Post");

        assertEquals("SELECT * FROM Post", actual, "output is the same as the input");
        verify(statementParser, description("parser is called")).parseSqlStatement("SELECT * FROM Post", model);
    }

    @Test
    public void _notify_increments() {
        model.notifySelectStatement();
        model.notifyInsertStatement();
        model.notifyUpdateStatement();
        model.notifyDeleteStatement();

        assertEquals(1, HibernateStatementCountInspector.getSelectStatementCount(), "the count is incremented");
        assertEquals(1, HibernateStatementCountInspector.getInsertStatementCount(), "the count is incremented");
        assertEquals(1, HibernateStatementCountInspector.getUpdateStatementCount(), "the count is incremented");
        assertEquals(1, HibernateStatementCountInspector.getDeleteStatementCount(), "the count is incremented");

        HibernateStatementCountInspector.resetCounts();

        assertEquals(0, HibernateStatementCountInspector.getSelectStatementCount(), "the count is reset");
        assertEquals(0, HibernateStatementCountInspector.getInsertStatementCount(), "the count is reset");
        assertEquals(0, HibernateStatementCountInspector.getUpdateStatementCount(), "the count is reset");
        assertEquals(0, HibernateStatementCountInspector.getDeleteStatementCount(), "the count is reset");
    }
}
