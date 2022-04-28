package com.mickaelb.integration.spring;

import com.mickaelb.api.AssertHibernateSQLCount;
import com.mickaelb.integration.hibernate.HibernateStatementStatistics;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestContext;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.description;

@ExtendWith(MockitoExtension.class)
class HibernateSQLCountTestListenerTest {

    public static class FakeClass {
        @AssertHibernateSQLCount
        public void annotatedMethod() {

        }

        public void notAnnotatedMethod() {

        }
    }

    @InjectMocks
    HibernateSQLCountTestListener model;

    @Mock
    Supplier<HibernateStatementStatistics> statisticsSupplier;

    @Mock
    Supplier<Boolean> transactionAvailabilitySupplier;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TestContext testContext;

    @Test
    public void _beforeTestMethod_no_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(HibernateSQLCountTestListenerTest.FakeClass.class.getMethod("notAnnotatedMethod"));
        when(statisticsSupplier.get()).thenReturn(mock(HibernateStatementStatistics.class));

        model.beforeTestMethod(testContext);

        verify(statisticsSupplier.get(), times(0).description("the reset method is NOT called")).resetStatistics();
    }

    @Test
    public void _beforeTestMethod_with_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(HibernateSQLCountTestListenerTest.FakeClass.class.getMethod("annotatedMethod"));
        when(statisticsSupplier.get()).thenReturn(mock(HibernateStatementStatistics.class));

        model.beforeTestMethod(testContext);

        verify(statisticsSupplier.get(), description("the reset method is called")).resetStatistics();
    }

    @Test
    public void _afterTestMethod_without_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(HibernateSQLCountTestListenerTest.FakeClass.class.getMethod("notAnnotatedMethod"));

        model.afterTestMethod(testContext);

        verify(testContext, times(0).description("EntityManager is NOT flushed")).getApplicationContext();
    }

    @Test
    public void _afterTestMethod_with_annotation_without_transaction() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(HibernateSQLCountTestListenerTest.FakeClass.class.getMethod("annotatedMethod"));
        when(transactionAvailabilitySupplier.get()).thenReturn(false);
        when(statisticsSupplier.get()).thenReturn(mock(HibernateStatementStatistics.class));

        model.afterTestMethod(testContext);

        verify(testContext, times(0).description("EntityManager is NOT flushed")).getApplicationContext();
        verify(statisticsSupplier.get(), description("statements are fetched")).getSelectStatements();
        verify(statisticsSupplier.get(), description("statements are fetched")).getUpdateStatements();
        verify(statisticsSupplier.get(), description("statements are fetched")).getInsertStatements();
        verify(statisticsSupplier.get(), description("statements are fetched")).getDeleteStatements();
    }

    @Test
    public void _afterTestMethod_with_annotation_with_transaction() throws NoSuchMethodException {
        EntityManager entityManager = mock(EntityManager.class);
        when(testContext.getTestMethod()).thenReturn(HibernateSQLCountTestListenerTest.FakeClass.class.getMethod("annotatedMethod"));
        when(testContext.getApplicationContext().getAutowireCapableBeanFactory().getBean(EntityManager.class)).thenReturn(entityManager);
        when(statisticsSupplier.get()).thenReturn(mock(HibernateStatementStatistics.class));
        when(transactionAvailabilitySupplier.get()).thenReturn(true);

        model.afterTestMethod(testContext);

        verify(entityManager, description("EntityManager is flushed")).flush();
        verify(statisticsSupplier.get(), description("statements are fetched")).getSelectStatements();
        verify(statisticsSupplier.get(), description("statements are fetched")).getUpdateStatements();
        verify(statisticsSupplier.get(), description("statements are fetched")).getInsertStatements();
        verify(statisticsSupplier.get(), description("statements are fetched")).getDeleteStatements();
    }
}
