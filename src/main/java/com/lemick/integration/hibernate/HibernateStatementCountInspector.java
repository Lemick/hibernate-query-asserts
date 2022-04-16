package com.lemick.integration.hibernate;

import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * NOT Thread-Safe since it is meant to be used by Spring tests that are not multi-threaded
 * ThreadLocal does not work because a test can span on multiple threads (ex: a test executing a HTTP request on the server)
 */
public class HibernateStatementCountInspector implements StatementInspector {

    private static final HibernateStatistics statistics = new HibernateStatistics();

    private HibernateStatementParser statementParser = new JSQLHibernateStatementParser();

    @Override
    public String inspect(String sql) {
        statementParser.parseSqlStatement(sql, statistics);
        return sql;
    }

    public static HibernateStatistics getStatistics() {
        return statistics;
    }

}
