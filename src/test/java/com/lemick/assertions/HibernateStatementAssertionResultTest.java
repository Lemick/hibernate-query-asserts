package com.lemick.assertions;

import org.junit.jupiter.api.Test;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.SELECT;
import static org.junit.jupiter.api.Assertions.*;

class HibernateStatementAssertionResultTest {

    @Test
    public void _isInError_true() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, 0, 1);

        assertTrue(model.isInError(), "the result is in error");
        assertEquals("Expected 1 SELECT but was 0", model.getErrorMessage(), "the error message is correct");
    }

    @Test
    public void _isInError_false() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, 1, 1);

        assertFalse(model.isInError(), "the result is not in error");
    }

    @Test
    public void _validate_does_not_throw() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, 1, 1);

        assertDoesNotThrow(model::validate);
    }

    @Test
    public void _validate_throws() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, 2, 1);

        assertThrows(HibernateStatementCountException.class, model::validate);
    }
}
