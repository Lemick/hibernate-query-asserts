package com.mickaelb.integration.spring.assertions.sql;

import com.mickaelb.integration.spring.assertions.HibernateStatementAssertionValidator;
import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;

import java.util.List;
import java.util.stream.Collectors;

public class HibernateStatementAssertionResults implements HibernateStatementAssertionValidator {

    private final List<HibernateStatementAssertionResult> assertionResults;

    public HibernateStatementAssertionResults(List<HibernateStatementAssertionResult> assertionResults) {
        this.assertionResults = assertionResults;
    }

    @Override
    public void validate() {
        List<HibernateStatementAssertionResult> assertionsInError = assertionResults.stream()
                .filter(HibernateStatementAssertionResult::isInError)
                .collect(Collectors.toList());

        if (!assertionsInError.isEmpty()) {
            String errorMessages = System.lineSeparator() + assertionsInError.stream()
                    .map(HibernateStatementAssertionResult::getErrorMessage)
                    .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));
            throw new HibernateAssertCountException(errorMessages);
        }
    }
}
