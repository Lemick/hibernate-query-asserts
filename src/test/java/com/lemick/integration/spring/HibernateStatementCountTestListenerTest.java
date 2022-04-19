package com.lemick.integration.spring;

import com.lemick.api.AssertHibernateSQLStatementCount;
import com.lemick.assertions.HibernateStatementAssertionResult;
import com.lemick.assertions.HibernateStatementAssertionsProvider;
import com.lemick.integration.hibernate.HibernateStatistics;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestContext;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateStatementCountTestListenerTest {

    public static class FakeClass {
        @AssertHibernateSQLStatementCount(inserts = 1, deletes = 2, selects = 3, updates = 4)
        public void annotatedMethod() {

        }

        public void notAnnotatedMethod() {

        }
    }

    @InjectMocks
    HibernateStatementCountTestListener model;

    @Mock
    HibernateStatementAssertionsProvider hibernateStatementAssertUtils;

    @Mock
    Supplier<HibernateStatistics> statisticsSupplier;

    @Mock
    Supplier<Boolean> transactionAvailabilitySupplier;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TestContext testContext;

    @Test
    public void _beforeTestMethod_no_annotation() {
        when(statisticsSupplier.get()).thenReturn(mock(HibernateStatistics.class));

        model.beforeTestMethod(testContext);

        verify(statisticsSupplier.get(), description("the reset method is called")).resetStatistics();
    }

    @Test
    public void _beforeTestMethod_with_annotation() {
        when(statisticsSupplier.get()).thenReturn(mock(HibernateStatistics.class));

        model.beforeTestMethod(testContext);

        verify(statisticsSupplier.get(), description("the reset method is called")).resetStatistics();
    }

    @Test
    public void _afterTestMethod_without_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("notAnnotatedMethod"));

        model.afterTestMethod(testContext);

        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).generateSelectStatementAssertion(anyInt(), any());
        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).generateUpdateStatementAssertion(anyInt(), any());
        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).generateInsertStatementAssertion(anyInt(), any());
        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).generateDeleteStatementAssertion(anyInt(), any());
    }

    @Test
    public void _afterTestMethod_with_annotation_without_transaction() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("annotatedMethod"));
        when(transactionAvailabilitySupplier.get()).thenReturn(false);

        HibernateStatementAssertionResult statementAssertionResult = mock(HibernateStatementAssertionResult.class);
        when(hibernateStatementAssertUtils.generateInsertStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.generateSelectStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.generateUpdateStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.generateDeleteStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);

        model.afterTestMethod(testContext);

        verify(testContext, times(0).description("EntityManager is NOT flushed")).getApplicationContext();
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateSelectStatementAssertion(3, statisticsSupplier);
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateUpdateStatementAssertion(4, statisticsSupplier);
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateInsertStatementAssertion(1, statisticsSupplier);
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateDeleteStatementAssertion(2, statisticsSupplier);
    }

    @Test
    public void _afterTestMethod_with_annotation_with_transaction() throws NoSuchMethodException {
        EntityManager entityManager = mock(EntityManager.class);
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("annotatedMethod"));
        when(testContext.getApplicationContext().getAutowireCapableBeanFactory().getBean(EntityManager.class)).thenReturn(entityManager);
        when(transactionAvailabilitySupplier.get()).thenReturn(true);

        HibernateStatementAssertionResult statementAssertionResult = mock(HibernateStatementAssertionResult.class);
        when(hibernateStatementAssertUtils.generateInsertStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.generateSelectStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.generateUpdateStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.generateDeleteStatementAssertion(anyInt(), any())).thenReturn(statementAssertionResult);

        model.afterTestMethod(testContext);

        verify(entityManager, description("EntityManager is flushed")).flush();
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateSelectStatementAssertion(3, statisticsSupplier);
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateUpdateStatementAssertion(4, statisticsSupplier);
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateInsertStatementAssertion(1, statisticsSupplier);
        verify(hibernateStatementAssertUtils, description("assert method is called")).generateDeleteStatementAssertion(2, statisticsSupplier);
    }
}
