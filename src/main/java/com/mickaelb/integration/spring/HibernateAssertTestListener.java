package com.mickaelb.integration.spring;


import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.List;
import java.util.Objects;

public class HibernateAssertTestListener implements TestExecutionListener, Ordered {

    private List<AssertTestListener> testListeners = List.of(new HibernateSQLCountTestListener(), new HibernateL2CCountTestListener());

    @Override
    public void beforeTestClass(TestContext testContext) {
        testListeners.forEach(assertTestListener -> assertTestListener.beforeTestClass(testContext));
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        testListeners.forEach(assertTestListener -> assertTestListener.beforeTestMethod(testContext));
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        testListeners.forEach(assertTestListener -> assertTestListener.afterTestMethod(testContext));
    }

    /**
     * Low precedence for executing before {@link org.springframework.test.context.transaction.TransactionalTestExecutionListener}
     * closes the transaction and to have the ability to flush the EntityManager
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public void setTestListeners(List<AssertTestListener> testListeners) {
        Objects.requireNonNull(testListeners);
        this.testListeners = testListeners;
    }
}
