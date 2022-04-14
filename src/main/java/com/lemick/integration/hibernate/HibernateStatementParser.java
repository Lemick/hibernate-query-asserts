package com.lemick.integration.hibernate;

public interface HibernateStatementParser {

    void parseSqlStatement(String sql, HibernateStatementCountListener statementCountListener);
}
