package com.lemick.assertions;

import org.junit.jupiter.api.Test;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.SELECT;
import static org.junit.jupiter.api.Assertions.*;

class HibernateStatementAssertionResultTest {

    @Test
    public void _result_in_error() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, 0, 1);

        assertTrue(model.isInError(), "the result is in error");
        assertEquals("Expected 1 SELECT but was 0", model.getErrorMessage(), "the error message is correct");
    }

    @Test
    public void _result_not_in_error() {
        HibernateStatementAssertionResult model = new HibernateStatementAssertionResult(SELECT, 1, 1);

        assertFalse(model.isInError(), "the result is not in error");
    }
}
