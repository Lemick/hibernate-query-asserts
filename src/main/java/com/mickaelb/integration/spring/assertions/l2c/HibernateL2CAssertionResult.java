package com.mickaelb.integration.spring.assertions.l2c;

import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;
import com.mickaelb.integration.spring.assertions.HibernateStatementAssertionValidator;

public class HibernateL2CAssertionResult implements HibernateStatementAssertionValidator {

    public enum CacheAction {HIT, MISS, PUT}

    private CacheAction type;
    private long actual;
    private long expected;

    public HibernateL2CAssertionResult(CacheAction type, long actual, long expected) {
        this.type = type;
        this.actual = actual;
        this.expected = expected;
    }

    public boolean isInError() {
        return actual != expected;
    }

    public String getErrorMessage() {
        return "Expected " + expected + " L2C cache " + type.name() + " but got " + actual;
    }

    @Override
    public void validate() {
        if (this.isInError()) {
            throw new HibernateAssertCountException(this.getErrorMessage());
        }
    }
}
