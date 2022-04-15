package com.lemick.assertions;

public class HibernateStatementAssertionResult implements HibernateStatementAssertionValidator {

    public enum StatementType {SELECT, INSERT, UPDATE, DELETE}

    private StatementType type;
    private int actual;
    private int expected;

    public HibernateStatementAssertionResult(StatementType type, int actual, int expected) {
        this.type = type;
        this.actual = actual;
        this.expected = expected;
    }

    public boolean isInError() {
        return actual != expected;
    }

    public String getErrorMessage() {
        return "Expected " + expected + " " + type.name() + " but was " + actual;
    }

    @Override
    public void validate() {
        if (this.isInError()) {
            throw new HibernateStatementCountException(this.getErrorMessage());
        }
    }
}
