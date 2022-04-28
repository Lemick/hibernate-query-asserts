package com.mickaelb.integration.hibernate;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public class JSQLHibernateStatementParser implements HibernateStatementParser {

    @Override
    public void parseSqlStatement(String sql, HibernateStatementListener statementCountListener) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            statement.accept(new StatementVisitorAdapter() {
                @Override
                public void visit(Select select) {
                    statementCountListener.notifySelectStatement(sql);
                }

                @Override
                public void visit(Insert insert) {
                    statementCountListener.notifyInsertStatement(sql);
                }

                @Override
                public void visit(Update update) {
                    statementCountListener.notifyUpdateStatement(sql);
                }

                @Override
                public void visit(Delete delete) {
                    statementCountListener.notifyDeleteStatement(sql);
                }
            });
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }
}
