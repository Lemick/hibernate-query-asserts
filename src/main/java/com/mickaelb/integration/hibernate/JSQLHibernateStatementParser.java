package com.mickaelb.integration.hibernate;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public class JSQLHibernateStatementParser implements HibernateStatementParser {

    @Override
    public void parseSqlStatement(String sql, HibernateStatementListener statementCountListener) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Delete) {
                statementCountListener.notifyDeleteStatement(sql);
            } else if (statement instanceof Insert) {
                statementCountListener.notifyInsertStatement(sql);
            } else if (statement instanceof Select) {
                statementCountListener.notifySelectStatement(sql);
            } else if (statement instanceof Update) {
                statementCountListener.notifyUpdateStatement(sql);
            }
        } catch (JSQLParserException ignored) {
        }
    }
}
