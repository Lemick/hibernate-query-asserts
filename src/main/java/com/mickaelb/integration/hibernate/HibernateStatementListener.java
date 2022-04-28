package com.mickaelb.integration.hibernate;

public interface HibernateStatementListener {

    void notifySelectStatement(String sql);
    void notifyUpdateStatement(String sql);
    void notifyInsertStatement(String sql);
    void notifyDeleteStatement(String sql);
}
