package com.lemick.assertions;


import com.lemick.integration.hibernate.HibernateStatementCountInspector;

import static com.lemick.assertions.HibernateStatementAssertionResult.StatementType.*;

public class HibernateStatementAssertUtils {

    public static HibernateStatementAssertionResult assertInsertStatementCount(int expectedInsertStatementCount) {
        return new HibernateStatementAssertionResult(SELECT, HibernateStatementCountInspector.getInsertStatementCount(), expectedInsertStatementCount);
    }

    public static HibernateStatementAssertionResult assertUpdateStatementCount(int expectedUpdateStatementCount) {
        return new HibernateStatementAssertionResult(UPDATE, HibernateStatementCountInspector.getUpdateStatementCount(), expectedUpdateStatementCount);
    }

    public static HibernateStatementAssertionResult assertSelectStatementCount(int expectedSelectStatementCount) {
        return new HibernateStatementAssertionResult(SELECT, HibernateStatementCountInspector.getSelectStatementCount(), expectedSelectStatementCount);
    }

    public static HibernateStatementAssertionResult assertDeleteStatementCount(int expectedDeleteStatementCount) {
        return new HibernateStatementAssertionResult(DELETE, HibernateStatementCountInspector.getDeleteStatementCount(), expectedDeleteStatementCount);
    }
}
