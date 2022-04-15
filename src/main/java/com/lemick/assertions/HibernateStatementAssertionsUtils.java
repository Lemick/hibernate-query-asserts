package com.lemick.assertions;


import com.lemick.integration.hibernate.HibernateStatementCountInspector;
import com.lemick.integration.hibernate.HibernateStatistics;

import java.util.function.Supplier;

/**
 * Static assertion class usable in tests
 */
public class HibernateStatementAssertionsUtils {

    private static final HibernateStatementAssertionsProvider hibernateStatementAssertionsProvider = new HibernateStatementAssertionsProvider();
    private static final Supplier<HibernateStatistics> statisticsSupplier = HibernateStatementCountInspector::getStatistics;

    public static void assertInsertStatementCount(int expectedInsertStatementCount) {
        hibernateStatementAssertionsProvider.generateInsertStatementAssertion(expectedInsertStatementCount, statisticsSupplier).validate();
    }

    public static void assertUpdateStatementCount(int expectedUpdateStatementCount) {
        hibernateStatementAssertionsProvider.generateUpdateStatementAssertion(expectedUpdateStatementCount, statisticsSupplier).validate();
    }

    public static void assertSelectStatementCount(int expectedSelectStatementCount) {
        hibernateStatementAssertionsProvider.generateSelectStatementAssertion(expectedSelectStatementCount, statisticsSupplier).validate();
    }

    public static void assertDeleteStatementCount(int expectedDeleteStatementCount) {
        hibernateStatementAssertionsProvider.generateDeleteStatementAssertion(expectedDeleteStatementCount, statisticsSupplier).validate();
    }
}
