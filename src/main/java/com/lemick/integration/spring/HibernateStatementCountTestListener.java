package com.lemick.integration.spring;


import com.lemick.api.AssertSQLStatementCount;
import com.lemick.assertions.HibernateStatementAssertUtils;
import com.lemick.assertions.HibernateStatementAssertionResults;
import com.lemick.integration.hibernate.HibernateStatementCountInspector;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.List;

public class HibernateStatementCountTestListener implements TestExecutionListener, Ordered {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        AssertSQLStatementCount annotation = testContext.getTestMethod().getAnnotation(AssertSQLStatementCount.class);
        if (annotation != null) {
            HibernateStatementCountInspector.resetCounts();
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        AssertSQLStatementCount annotation = testContext.getTestMethod().getAnnotation(AssertSQLStatementCount.class);
        if (annotation != null) {
            HibernateStatementAssertionResults assertionResults = new HibernateStatementAssertionResults(List.of(
                    HibernateStatementAssertUtils.assertSelectStatementCount(annotation.selects()),
                    HibernateStatementAssertUtils.assertUpdateStatementCount(annotation.updates()),
                    HibernateStatementAssertUtils.assertInsertStatementCount(annotation.inserts()),
                    HibernateStatementAssertUtils.assertDeleteStatementCount(annotation.deletes())
            ));
            assertionResults.validate();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
