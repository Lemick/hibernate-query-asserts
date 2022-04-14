package com.lemick.assertions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.*;
import static org.junit.jupiter.api.Assertions.*;

class HibernateStatementAssertionResultsTest {

    @Test
    public void _results_empty() {
        HibernateStatementAssertionResults model = new HibernateStatementAssertionResults(List.of());

        assertDoesNotThrow(model::validate, "an empty list does not throw an error");
    }

    @Test
    public void _results_invalid() {
        HibernateStatementAssertionResults model = new HibernateStatementAssertionResults(List.of(
                new HibernateStatementAssertionResult(SELECT, 0, 2),
                new HibernateStatementAssertionResult(INSERT, 1, 1),
                new HibernateStatementAssertionResult(DELETE, 4, 2),
                new HibernateStatementAssertionResult(UPDATE, 3, 2)
        ));

        HibernateStatementCountException actual = assertThrows(HibernateStatementCountException.class, model::validate, "validation error is thrown");

        String expected = "Expected 2 SELECT but was 0" + System.lineSeparator() +
                "Expected 2 DELETE but was 4" + System.lineSeparator() +
                "Expected 2 UPDATE but was 3";
        assertEquals(expected, actual.getMessage(), "the error message is correct");
    }
}
