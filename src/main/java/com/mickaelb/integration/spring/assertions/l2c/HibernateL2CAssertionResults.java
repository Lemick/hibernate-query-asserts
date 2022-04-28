package com.mickaelb.integration.spring.assertions.l2c;

import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;
import com.mickaelb.integration.spring.assertions.HibernateStatementAssertionValidator;

import java.util.List;
import java.util.stream.Collectors;

public class HibernateL2CAssertionResults implements HibernateStatementAssertionValidator {

    private final List<HibernateL2CAssertionResult> assertionResults;

    public HibernateL2CAssertionResults(List<HibernateL2CAssertionResult> assertionResults) {
        this.assertionResults = assertionResults;
    }

    @Override
    public void validate() {
        List<HibernateL2CAssertionResult> assertionsInError = assertionResults.stream()
                .filter(HibernateL2CAssertionResult::isInError)
                .collect(Collectors.toList());

        if (assertionsInError.size() > 0) {
            String errorMessages = System.lineSeparator() + assertionsInError.stream()
                    .map(HibernateL2CAssertionResult::getErrorMessage)
                    .collect(Collectors.joining(System.lineSeparator()));
            throw new HibernateAssertCountException(errorMessages);
        }
    }
}
