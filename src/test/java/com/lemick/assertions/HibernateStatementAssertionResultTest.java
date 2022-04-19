package com.lemick.assertions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.SELECT;
import static org.junit.jupiter.api.Assertions.*;

class HibernateStatementAssertionResultTest {

    @Test
    public void _isInError_true() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, List.of("SELECT 1", "SELECT 2"), 1);

        assertTrue(model.isInError(), "the result is in error");
    }

    @Test
    public void _isInError_false() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, List.of("SELECT 1"), 1);

        assertFalse(model.isInError(), "the result is not in error");
    }

    @Test
    public void _validate_does_not_throw() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, List.of("SELECT 1"), 1);

        assertDoesNotThrow(model::validate);
    }

    @Test
    public void _validate_throws() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, List.of("SELECT 1", "SELECT 2"), 1);

        assertThrows(HibernateStatementCountException.class, model::validate);
    }

    @Test
    public void _getErrorMessage() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, List.of("SELECT 1", "SELECT 2"), 1);

        String expected = "Expected 1 SELECT but got 2:" + System.lineSeparator() +
                "     => 'SELECT 1'" + System.lineSeparator() +
                "     => 'SELECT 2'";
        assertEquals(expected, model.getErrorMessage(), "the error message is correct");
    }
}
