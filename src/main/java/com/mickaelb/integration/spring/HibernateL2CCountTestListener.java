package com.mickaelb.integration.spring;

import com.mickaelb.api.AssertHibernateL2CCount;
import com.mickaelb.integration.spring.assertions.l2c.HibernateL2CAssertionResult;
import com.mickaelb.integration.spring.assertions.l2c.HibernateL2CAssertionResults;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.test.context.TestContext;

import java.util.List;

import static com.mickaelb.integration.spring.assertions.l2c.HibernateL2CAssertionResult.CacheAction.*;

public class HibernateL2CCountTestListener implements AssertTestListener {

    private Statistics sessionFactoryStatistics;

    @Override
    public void beforeTestClass(TestContext testContext) {
        SessionFactory sessionFactory = testContext.getApplicationContext()
                .getAutowireCapableBeanFactory()
                .getBean(SessionFactory.class);

        sessionFactoryStatistics = sessionFactory.getStatistics();
        sessionFactoryStatistics.setStatisticsEnabled(true);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        sessionFactoryStatistics.clear();
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        AssertHibernateL2CCount l2cCountAnnotation = testContext.getTestMethod().getAnnotation(AssertHibernateL2CCount.class);
        if (l2cCountAnnotation != null) {
            evaluateL2CCount(l2cCountAnnotation);
        }
    }

    private void evaluateL2CCount(AssertHibernateL2CCount annotation) {
        HibernateL2CAssertionResults assertionResults = new HibernateL2CAssertionResults(List.of(
                new HibernateL2CAssertionResult(HIT, sessionFactoryStatistics.getSecondLevelCacheHitCount(), annotation.hits()),
                new HibernateL2CAssertionResult(MISS, sessionFactoryStatistics.getSecondLevelCacheMissCount(), annotation.misses()),
                new HibernateL2CAssertionResult(PUT, sessionFactoryStatistics.getSecondLevelCachePutCount(), annotation.puts())
        ));
        assertionResults.validate();
    }

    Statistics getSessionFactoryStatistics() {
        return sessionFactoryStatistics;
    }

    void setSessionFactoryStatistics(Statistics sessionFactoryStatistics) {
        this.sessionFactoryStatistics = sessionFactoryStatistics;
    }
}
