package com.lemick.integration.hibernate;

public class HibernateStatistics implements HibernateStatementCountListener{

    private int selectStatementCount = 0;
    private int updateStatementCount = 0;
    private int insertStatementCount = 0;
    private int deleteStatementCount = 0;

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

    public int getSelectStatementCount() {
        return selectStatementCount;
    }

    public int getUpdateStatementCount() {
        return updateStatementCount;
    }

    public int getInsertStatementCount() {
        return insertStatementCount;
    }

    public int getDeleteStatementCount() {
        return deleteStatementCount;
    }
}
