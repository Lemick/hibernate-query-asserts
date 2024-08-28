package com.mickaelb.integration.spring;

import com.mickaelb.api.AssertHibernateSQLCount;
import com.mickaelb.integration.hibernate.HibernateStatementInspector;
import com.mickaelb.integration.spring.assertions.sql.HibernateStatementAssertionResult;
import com.mickaelb.integration.spring.assertions.sql.HibernateStatementAssertionResults;
import com.mickaelb.integration.hibernate.HibernateStatementStatistics;
import jakarta.persistence.EntityManager;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.List;
import java.util.function.Supplier;

import static com.mickaelb.api.StatementType.*;

public class HibernateSQLCountTestListener implements AssertTestListener{

    private Supplier<HibernateStatementStatistics> statisticsSupplier = HibernateStatementInspector::getStatistics;
    private Supplier<Boolean> transactionAvailabilitySupplier = TestTransaction::isActive;

    @Override
    public void beforeTestClass(TestContext testContext) {
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        statisticsSupplier.get().resetStatistics();
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        AssertHibernateSQLCount sqlCountAnnotation = testContext.getTestMethod().getAnnotation(AssertHibernateSQLCount.class);
        if (sqlCountAnnotation != null) {
            flushExistingPersistenceContext(testContext, transactionAvailabilitySupplier);
            evaluateSQLStatementCount(sqlCountAnnotation);
        }
    }

    private void flushExistingPersistenceContext(TestContext testContext, Supplier<Boolean> transactionAvailabilitySupplier) {
        if (transactionAvailabilitySupplier.get()) {
            EntityManager entityManager = testContext.getApplicationContext()
                    .getAutowireCapableBeanFactory()
                    .getBean(EntityManager.class);
            entityManager.flush();
        }
    }

    private void evaluateSQLStatementCount(AssertHibernateSQLCount annotation) {
        HibernateStatementAssertionResults assertionResults = new HibernateStatementAssertionResults(List.of(
                new HibernateStatementAssertionResult(SELECT, statisticsSupplier.get().getSelectStatements(), annotation.selects()),
                new HibernateStatementAssertionResult(UPDATE, statisticsSupplier.get().getUpdateStatements(), annotation.updates()),
                new HibernateStatementAssertionResult(INSERT, statisticsSupplier.get().getInsertStatements(), annotation.inserts()),
                new HibernateStatementAssertionResult(DELETE, statisticsSupplier.get().getDeleteStatements(), annotation.deletes())
        ));
        assertionResults.validate();
    }

}
