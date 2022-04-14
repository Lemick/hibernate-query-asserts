package com.lemick.integration.hibernate;

public interface HibernateStatementCountListener {

    void notifySelectStatement();
    void notifyUpdateStatement();
    void notifyInsertStatement();
    void notifyDeleteStatement();
}
