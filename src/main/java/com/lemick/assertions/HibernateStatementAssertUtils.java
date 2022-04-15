package com.lemick.assertions;


import com.lemick.integration.hibernate.HibernateStatementCountInspector;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.*;

public class HibernateStatementAssertUtils {

    public HibernateStatementAssertionResult assertInsertStatementCount(int expectedInsertStatementCount) {
        return new HibernateStatementAssertionResult(INSERT, HibernateStatementCountInspector.getStatistics().getInsertStatementCount(), expectedInsertStatementCount);
    }

    public HibernateStatementAssertionResult assertUpdateStatementCount(int expectedUpdateStatementCount) {
        return new HibernateStatementAssertionResult(UPDATE, HibernateStatementCountInspector.getStatistics().getUpdateStatementCount(), expectedUpdateStatementCount);
    }

    public HibernateStatementAssertionResult assertSelectStatementCount(int expectedSelectStatementCount) {
        return new HibernateStatementAssertionResult(SELECT, HibernateStatementCountInspector.getStatistics().getSelectStatementCount(), expectedSelectStatementCount);
    }

    public HibernateStatementAssertionResult assertDeleteStatementCount(int expectedDeleteStatementCount) {
        return new HibernateStatementAssertionResult(DELETE, HibernateStatementCountInspector.getStatistics().getDeleteStatementCount(), expectedDeleteStatementCount);
    }

    public void resetCounts() {
        HibernateStatementCountInspector.resetStatistics();
    }
}
