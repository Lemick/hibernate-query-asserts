package com.mickaelb.integration.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JSQLHibernateStatementParserTest {

    @InjectMocks
    JSQLHibernateStatementParser model;

    @Mock
    HibernateStatementListener hibernateStatementListener;

    @Test
    public void _parseSqlStatement_select() {
        model.parseSqlStatement("SELECT * FROM Post", hibernateStatementListener);

        verify(hibernateStatementListener, description("the correct notifier is called")).notifySelectStatement("SELECT * FROM Post");
    }

    @Test
    public void _parseSqlStatement_update() {
        model.parseSqlStatement("UPDATE Post p SET p.title = ?", hibernateStatementListener);

        verify(hibernateStatementListener, description("the correct notifier is called")).notifyUpdateStatement("UPDATE Post p SET p.title = ?");
    }

    @Test
    public void _parseSqlStatement_insert() {
        model.parseSqlStatement("INSERT INTO Post VALUES (?)", hibernateStatementListener);

        verify(hibernateStatementListener, description("the correct notifier is called")).notifyInsertStatement("INSERT INTO Post VALUES (?)");
    }

    @Test
    public void _parseSqlStatement_delete() {
        model.parseSqlStatement("DELETE FROM Post", hibernateStatementListener);

        verify(hibernateStatementListener, description("the correct notifier is called")).notifyDeleteStatement("DELETE FROM Post");
    }

    @Test
    public void _incorrect_statement_should_not_throw() {
        model.parseSqlStatement("This is not really SQL", hibernateStatementListener);

        verifyNoInteractions(hibernateStatementListener);
    }
}
