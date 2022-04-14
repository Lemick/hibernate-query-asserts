package com.lemick.integration.hibernate;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class HibernateStatementCountInspector implements StatementInspector, HibernateStatementCountListener {

    private static int selectStatementCount = 0;
    private static int updateStatementCount = 0;
    private static int insertStatementCount = 0;
    private static int deleteStatementCount = 0;

    private HibernateStatementParser statementParser = new JSQLHibernateStatementParser();

    @Override
    public String inspect(String sql) {
        statementParser.parseSqlStatement(sql, this);
        return sql;
    }

    @Override
    public void notifySelectStatement() {
        selectStatementCount++;
    }

    @Override
    public void notifyUpdateStatement() {
        updateStatementCount++;
    }

    @Override
    public void notifyInsertStatement() {
        insertStatementCount++;
    }

    @Override
    public void notifyDeleteStatement() {
        deleteStatementCount++;
    }

    public static void resetCounts() {
        selectStatementCount = 0;
        updateStatementCount = 0;
        insertStatementCount = 0;
        deleteStatementCount = 0;
    }

    public static int getSelectStatementCount() {
        return selectStatementCount;
    }

    public static int getUpdateStatementCount() {
        return updateStatementCount;
    }

    public static int getInsertStatementCount() {
        return insertStatementCount;
    }

    public static int getDeleteStatementCount() {
        return deleteStatementCount;
    }
}
