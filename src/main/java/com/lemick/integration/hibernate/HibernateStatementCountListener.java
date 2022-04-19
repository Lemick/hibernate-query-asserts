package com.lemick.integration.hibernate;

public interface HibernateStatementCountListener {

    void notifySelectStatement(String sql);
    void notifyUpdateStatement(String sql);
    void notifyInsertStatement(String sql);
    void notifyDeleteStatement(String sql);
}
