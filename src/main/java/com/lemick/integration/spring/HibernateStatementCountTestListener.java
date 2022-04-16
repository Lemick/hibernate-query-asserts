package com.lemick.integration.spring;


import com.lemick.api.AssertHibernateSQLStatementCount;
import com.lemick.assertions.HibernateStatementAssertionResults;
import com.lemick.assertions.HibernateStatementAssertionsProvider;
import com.lemick.integration.hibernate.HibernateStatementCountInspector;
import com.lemick.integration.hibernate.HibernateStatistics;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.List;
import java.util.function.Supplier;

public class HibernateStatementCountTestListener implements TestExecutionListener, Ordered {

    private HibernateStatementAssertionsProvider hibernateStatementAssertionsProvider = new HibernateStatementAssertionsProvider();
    private Supplier<HibernateStatistics> statisticsSupplier = HibernateStatementCountInspector::getStatistics;

    @Override
    public void beforeTestMethod(TestContext testContext) {
        statisticsSupplier.get().resetStatistics();
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        AssertHibernateSQLStatementCount annotation = testContext.getTestMethod().getAnnotation(AssertHibernateSQLStatementCount.class);
        if (annotation != null) {
            HibernateStatementAssertionResults assertionResults = new HibernateStatementAssertionResults(List.of(
                    hibernateStatementAssertionsProvider.generateSelectStatementAssertion(annotation.selects(), statisticsSupplier),
                    hibernateStatementAssertionsProvider.generateUpdateStatementAssertion(annotation.updates(), statisticsSupplier),
                    hibernateStatementAssertionsProvider.generateInsertStatementAssertion(annotation.inserts(), statisticsSupplier),
                    hibernateStatementAssertionsProvider.generateDeleteStatementAssertion(annotation.deletes(), statisticsSupplier)
            ));
            assertionResults.validate();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
