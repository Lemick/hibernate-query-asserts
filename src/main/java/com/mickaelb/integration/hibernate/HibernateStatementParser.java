package com.mickaelb.integration.hibernate;

public interface HibernateStatementParser {

    void parseSqlStatement(String sql, HibernateStatementListener statementCountListener);
}
