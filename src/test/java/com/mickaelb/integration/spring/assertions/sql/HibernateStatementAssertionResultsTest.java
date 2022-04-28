package com.mickaelb.integration.spring.assertions.sql;

import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;
import com.mickaelb.integration.spring.assertions.sql.HibernateStatementAssertionResult;
import com.mickaelb.integration.spring.assertions.sql.HibernateStatementAssertionResults;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mickaelb.integration.spring.assertions.sql.HibernateStatementAssertionResult.StatementType.*;
import static org.junit.jupiter.api.Assertions.*;

class HibernateStatementAssertionResultsTest {

    static final String EOL = System.lineSeparator();

    @Test
    public void _results_empty() {
        HibernateStatementAssertionResults model = new HibernateStatementAssertionResults(List.of());

        assertDoesNotThrow(model::validate, "an empty list does not throw an error");
    }

    @Test
    public void _results_invalid() {
        HibernateStatementAssertionResults model = new HibernateStatementAssertionResults(List.of(
                new HibernateStatementAssertionResult(SELECT, List.of("SELECT 1"), 2),
                new HibernateStatementAssertionResult(INSERT, List.of("INSERT 1","INSERT 2"), 1),
                new HibernateStatementAssertionResult(DELETE, List.of("DELETE 1"), 1),
                new HibernateStatementAssertionResult(UPDATE, List.of("UPDATE 1"), 2)
        ));

        HibernateAssertCountException actual = assertThrows(HibernateAssertCountException.class, model::validate, "validation error is thrown");

        String expected = EOL +
                "Expected 2 SELECT but got 1:" + EOL +
                "     => 'SELECT 1'" + EOL +
                EOL +
                "Expected 1 INSERT but got 2:" + EOL +
                "     => 'INSERT 1'" + EOL +
                "     => 'INSERT 2'" + EOL +
                EOL +
                "Expected 2 UPDATE but got 1:" + EOL +
                "     => 'UPDATE 1'";
        assertEquals(expected, actual.getMessage(), "the error message is correct");
    }
}
