package com.lemick.assertions;


import com.lemick.integration.hibernate.HibernateStatementCountInspector;
import com.lemick.integration.hibernate.HibernateStatistics;

import java.util.function.Supplier;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.*;

public class HibernateStatementAssertionsProvider {

    public HibernateStatementAssertionResult generateInsertStatementAssertion(int expectedInsertStatementCount, Supplier<HibernateStatistics> statisticsSupplier) {
        return new HibernateStatementAssertionResult(INSERT, statisticsSupplier.get().getInsertStatementCount(), expectedInsertStatementCount);
    }

    public HibernateStatementAssertionResult generateUpdateStatementAssertion(int expectedUpdateStatementCount, Supplier<HibernateStatistics> statisticsSupplier) {
        return new HibernateStatementAssertionResult(UPDATE, statisticsSupplier.get().getUpdateStatementCount(), expectedUpdateStatementCount);
    }

    public HibernateStatementAssertionResult generateSelectStatementAssertion(int expectedSelectStatementCount, Supplier<HibernateStatistics> statisticsSupplier) {
        return new HibernateStatementAssertionResult(SELECT, statisticsSupplier.get().getSelectStatementCount(), expectedSelectStatementCount);
    }

    public HibernateStatementAssertionResult generateDeleteStatementAssertion(int expectedDeleteStatementCount, Supplier<HibernateStatistics> statisticsSupplier) {
        return new HibernateStatementAssertionResult(DELETE, statisticsSupplier.get().getDeleteStatementCount(), expectedDeleteStatementCount);
    }
}
