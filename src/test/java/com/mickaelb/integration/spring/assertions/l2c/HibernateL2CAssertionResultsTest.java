package com.mickaelb.integration.spring.assertions.l2c;

import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mickaelb.integration.spring.assertions.l2c.HibernateL2CAssertionResult.CacheAction.*;
import static org.junit.jupiter.api.Assertions.*;

class HibernateL2CAssertionResultsTest {

    @Test
    public void _results_empty() {
        HibernateL2CAssertionResults model = new HibernateL2CAssertionResults(List.of());

        assertDoesNotThrow(model::validate, "an empty list does not throw an error");
    }

    @Test
    public void _results_invalid() {
        HibernateL2CAssertionResults model = new HibernateL2CAssertionResults(List.of(
                new HibernateL2CAssertionResult(HIT, 1, 2),
                new HibernateL2CAssertionResult(MISS, 2, 1),
                new HibernateL2CAssertionResult(PUT, 1, 1)
        ));

        HibernateAssertCountException actual = assertThrows(HibernateAssertCountException.class, model::validate, "validation error is thrown");

        String expected = System.lineSeparator() +
                "Expected 2 L2C cache HIT but got 1" + System.lineSeparator() +
                "Expected 1 L2C cache MISS but got 2";

        assertEquals(expected, actual.getMessage(), "the error message is correct");
    }
}
