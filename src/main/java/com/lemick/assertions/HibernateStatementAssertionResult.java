package com.lemick.assertions;

import java.util.List;
import java.util.stream.Collectors;

public class HibernateStatementAssertionResult implements HibernateStatementAssertionValidator {

    public enum StatementType {SELECT, INSERT, UPDATE, DELETE}

    private StatementType type;
    private List<String> statements;
    private int expected;

    public HibernateStatementAssertionResult(StatementType type, List<String> statements, int expected) {
        this.type = type;
        this.statements = statements;
        this.expected = expected;
    }

    public boolean isInError() {
        return statements.size() != expected;
    }

    public String getErrorMessage() {
        String header = "Expected " + expected + " " + type.name() + " but got " + statements.size() + ":" + System.lineSeparator();
        String statementsDetail = statements.stream()
                .map(statement -> "     => '" + statement + "'")
                .collect(Collectors.joining(System.lineSeparator()));
        return header + statementsDetail;
    }

    @Override
    public void validate() {
        if (this.isInError()) {
            throw new HibernateStatementCountException(this.getErrorMessage());
        }
    }
}
