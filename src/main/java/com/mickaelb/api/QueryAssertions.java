package com.mickaelb.api;

import com.mickaelb.integration.spring.assertions.sql.HibernateStatementAssertionResult;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mickaelb.api.StatementType.*;
import static com.mickaelb.integration.hibernate.HibernateStatementInspector.getStatistics;

public class QueryAssertions {

    private static final Map<StatementType, Supplier<List<String>>> STATEMENT_SUPPLIERS = Map.of(
            INSERT, () -> getStatistics().getInsertStatements(),
            UPDATE, () -> getStatistics().getUpdateStatements(),
            SELECT, () -> getStatistics().getSelectStatements(),
            DELETE, () -> getStatistics().getDeleteStatements()
    );

    private QueryAssertions() {
    }

    public static void assertInsertCount(int expectedInsertCount, Runnable runnable) {
        doAssertStatementCount(runnable, INSERT, expectedInsertCount);
    }

    public static void assertUpdateCount(int expectedUpdateCount, Runnable runnable) {
        doAssertStatementCount(runnable, UPDATE, expectedUpdateCount);
    }

    public static void assertSelectCount(int expectedSelectCount, Runnable runnable) {
        doAssertStatementCount(runnable, SELECT, expectedSelectCount);
    }

    public static void assertDeleteCount(int expectedDeleteCount, Runnable runnable) {
        doAssertStatementCount(runnable, DELETE, expectedDeleteCount);
    }

    public static void assertStatementCount(Map<StatementType, Integer> expectedCounts, Runnable runnable) {
        Map<StatementType, Integer> sizesBeforeExecution = expectedCounts.keySet().stream()
                .collect(Collectors.toMap(
                        statementType -> statementType,
                        statementType -> STATEMENT_SUPPLIERS.get(statementType).get().size()
                ));

        runnable.run();

        for (Map.Entry<StatementType, Integer> entry : expectedCounts.entrySet()) {
            StatementType statementType = entry.getKey();
            int expectedCount = entry.getValue();

            Supplier<List<String>> statementSupplier = STATEMENT_SUPPLIERS.get(statementType);
            List<String> fullStatements = statementSupplier.get();

            int sizeBeforeExecution = sizesBeforeExecution.get(statementType);
            List<String> executionScopedStatements = fullStatements.subList(sizeBeforeExecution, fullStatements.size());

            HibernateStatementAssertionResult assertionResult = new HibernateStatementAssertionResult(statementType, executionScopedStatements, expectedCount);
            assertionResult.validate();
        }
    }

    private static void doAssertStatementCount(Runnable runnable, StatementType statementType, int expectedCount) {
        Supplier<List<String>> statementSupplier = STATEMENT_SUPPLIERS.get(statementType);
        int sizeBeforeExecution = statementSupplier.get().size();
        runnable.run();

        List<String> fullStatements = statementSupplier.get();
        List<String> executionScopedStatements = fullStatements.subList(sizeBeforeExecution, fullStatements.size());

        HibernateStatementAssertionResult assertionResult = new HibernateStatementAssertionResult(statementType, executionScopedStatements, expectedCount);
        assertionResult.validate();
    }
}
