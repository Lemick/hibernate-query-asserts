package com.mickaelb.integration.spring;

import com.mickaelb.api.AssertHibernateL2CCount;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateL2CCountTestListenerTest {
    public static class FakeClass {
        @AssertHibernateL2CCount
        public void annotatedMethod() {

        }

        public void notAnnotatedMethod() {

        }
    }

    @InjectMocks
    HibernateL2CCountTestListener model;

    @Mock
    Statistics statistics;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TestContext testContext;

    @Test
    public void _beforeTestClass() {
        SessionFactory sessionFactory = mock(SessionFactory.class);
        when(sessionFactory.getStatistics()).thenReturn(statistics);
        when(testContext.getApplicationContext().getAutowireCapableBeanFactory().getBean(SessionFactory.class)).thenReturn(sessionFactory);
        model.setSessionFactoryStatistics(null);

        model.beforeTestClass(testContext);

        assertSame(statistics, model.getSessionFactoryStatistics(), "the statistics object is set");
        verify(statistics, description("statistics is explicitly enabled during initialization")).setStatisticsEnabled(true);

    }

    @Test
    public void _beforeTestMethod_no_annotation() {
        model.beforeTestMethod(testContext);

        verify(statistics, description("the statistics are cleared")).clear();
    }

    @Test
    public void _beforeTestMethod_with_annotation() {
        model.beforeTestMethod(testContext);

        verify(statistics, description("the statistics are cleared")).clear();
    }

    @Test
    public void _afterTestMethod_without_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(HibernateL2CCountTestListenerTest.FakeClass.class.getMethod("notAnnotatedMethod"));

        model.afterTestMethod(testContext);

        verify(statistics, times(0).description("The statistics count is not fetched")).getSecondLevelCacheHitCount();
        verify(statistics, times(0).description("The statistics count is not fetched")).getSecondLevelCacheMissCount();
        verify(statistics, times(0).description("The statistics count is not fetched")).getSecondLevelCachePutCount();
    }

    @Test
    public void _afterTestMethod_with_annotation_with_transaction() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(HibernateL2CCountTestListenerTest.FakeClass.class.getMethod("annotatedMethod"));

        model.afterTestMethod(testContext);

        verify(statistics, description("The statistics count is fetched")).getSecondLevelCacheHitCount();
        verify(statistics, description("The statistics count is fetched")).getSecondLevelCacheMissCount();
        verify(statistics, description("The statistics count is fetched")).getSecondLevelCachePutCount();
    }
}
