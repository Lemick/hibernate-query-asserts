package com.lemick.assertions;

import java.util.List;
import java.util.stream.Collectors;

public class HibernateStatementAssertionResults implements HibernateStatementAssertionValidator {

    private final List<HibernateStatementAssertionResult> assertionResults;

    public HibernateStatementAssertionResults(List<HibernateStatementAssertionResult> statementAssertionResults) {
        this.assertionResults = statementAssertionResults;
    }

    @Override
    public void validate() {
        List<HibernateStatementAssertionResult> assertionsInError = assertionResults.stream()
                .filter(HibernateStatementAssertionResult::isInError)
                .collect(Collectors.toList());

        if (assertionsInError.size() > 0) {
            String errorMessages = System.lineSeparator() + assertionsInError.stream()
                    .map(HibernateStatementAssertionResult::getErrorMessage)
                    .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));
            throw new HibernateStatementCountException(errorMessages);
        }
    }
}
