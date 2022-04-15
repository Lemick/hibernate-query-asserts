package com.lemick.integration.spring;


import com.lemick.api.AssertSQLStatementCount;
import com.lemick.assertions.HibernateStatementAssertUtils;
import com.lemick.assertions.HibernateStatementAssertionResults;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.List;

/**
 * TODO
 * Permettre les asserts dans le test
 * Augmenter les type d'assert (mise en cache, etc...)
 * Classe Thread-safe
 */
public class HibernateStatementCountTestListener implements TestExecutionListener, Ordered {

    private HibernateStatementAssertUtils hibernateStatementAssertUtils = new HibernateStatementAssertUtils();

    @Override
    public void beforeTestMethod(TestContext testContext) {
        AssertSQLStatementCount annotation = testContext.getTestMethod().getAnnotation(AssertSQLStatementCount.class);
        if (annotation != null) {
            hibernateStatementAssertUtils.resetCounts();
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        AssertSQLStatementCount annotation = testContext.getTestMethod().getAnnotation(AssertSQLStatementCount.class);
        if (annotation != null) {
            HibernateStatementAssertionResults assertionResults = new HibernateStatementAssertionResults(List.of(
                    hibernateStatementAssertUtils.assertSelectStatementCount(annotation.selects()),
                    hibernateStatementAssertUtils.assertUpdateStatementCount(annotation.updates()),
                    hibernateStatementAssertUtils.assertInsertStatementCount(annotation.inserts()),
                    hibernateStatementAssertUtils.assertDeleteStatementCount(annotation.deletes())
            ));
            assertionResults.validate();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
