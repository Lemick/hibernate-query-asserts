package com.lemick.integration.hibernate;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class HibernateStatementCountInspector implements StatementInspector {

    private static final ThreadLocal<HibernateStatistics> statisticsStore = ThreadLocal.withInitial(HibernateStatistics::new);

    private HibernateStatementParser statementParser = new JSQLHibernateStatementParser();

    @Override
    public String inspect(String sql) {
        statementParser.parseSqlStatement(sql, statisticsStore.get());
        return sql;
    }

    public static HibernateStatistics getStatistics() {
        return statisticsStore.get();
    }

}
