package com.mickaelb.integration.spring.assertions.l2c;

import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;
import org.junit.jupiter.api.Test;

import static com.mickaelb.integration.spring.assertions.l2c.HibernateL2CAssertionResult.CacheAction.HIT;
import static org.junit.jupiter.api.Assertions.*;

class HibernateL2CAssertionResultTest {

    @Test
    public void _isInError_true() {
        HibernateL2CAssertionResult model = new HibernateL2CAssertionResult(HIT, 2, 1);

        assertTrue(model.isInError(), "the result is in error");
    }

    @Test
    public void _isInError_false() {
        HibernateL2CAssertionResult model = new HibernateL2CAssertionResult(HIT, 1, 1);

        assertFalse(model.isInError(), "the result is not in error");
    }

    @Test
    public void _validate_does_not_throw() {
        HibernateL2CAssertionResult model = new HibernateL2CAssertionResult(HIT, 1, 1);

        assertDoesNotThrow(model::validate);
    }

    @Test
    public void _validate_throws() {
        HibernateL2CAssertionResult model = new HibernateL2CAssertionResult(HIT, 2, 1);

        assertThrows(HibernateAssertCountException.class, model::validate);
    }

    @Test
    public void _getErrorMessage() {
        HibernateL2CAssertionResult model = new HibernateL2CAssertionResult(HIT, 2, 1);

        String expected = "Expected 1 L2C cache HIT but got 2";
        assertEquals(expected, model.getErrorMessage(), "the error message is correct");
    }
}
