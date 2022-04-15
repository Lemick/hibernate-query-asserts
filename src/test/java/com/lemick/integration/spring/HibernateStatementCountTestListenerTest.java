package com.lemick.integration.spring;

import com.lemick.api.AssertSQLStatementCount;
import com.lemick.assertions.HibernateStatementAssertUtils;
import com.lemick.assertions.HibernateStatementAssertionResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestContext;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateStatementCountTestListenerTest {

    public class FakeClass {
        @AssertSQLStatementCount(inserts = 1, deletes = 2, selects = 3, updates = 4)
        public void annotatedMethod() {

        }

        public void notAnnotatedMethod() {

        }
    }

    @InjectMocks
    HibernateStatementCountTestListener model;

    @Mock
    HibernateStatementAssertUtils hibernateStatementAssertUtils;

    @Mock
    TestContext testContext;

    @Test
    public void _beforeTestMethod_no_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("notAnnotatedMethod"));

        model.beforeTestMethod(testContext);

        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).resetCounts();
    }

    @Test
    public void _beforeTestMethod_with_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("annotatedMethod"));

        model.beforeTestMethod(testContext);

        verify(hibernateStatementAssertUtils, description("the reset method is called")).resetCounts();
    }

    @Test
    public void _afterTestMethod_no_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("notAnnotatedMethod"));

        model.afterTestMethod(testContext);

        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).assertSelectStatementCount(anyInt());
        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).assertUpdateStatementCount(anyInt());
        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).assertInsertStatementCount(anyInt());
        verify(hibernateStatementAssertUtils, times(0).description("no call is done")).assertDeleteStatementCount(anyInt());
    }

    @Test
    public void _afterTestMethod_with_annotation() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("annotatedMethod"));

        HibernateStatementAssertionResult statementAssertionResult = mock(HibernateStatementAssertionResult.class);
        when(hibernateStatementAssertUtils.assertInsertStatementCount(anyInt())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.assertSelectStatementCount(anyInt())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.assertUpdateStatementCount(anyInt())).thenReturn(statementAssertionResult);
        when(hibernateStatementAssertUtils.assertDeleteStatementCount(anyInt())).thenReturn(statementAssertionResult);

        model.afterTestMethod(testContext);

        verify(hibernateStatementAssertUtils, description("assert method is called")).assertSelectStatementCount(3);
        verify(hibernateStatementAssertUtils, description("assert method is called")).assertUpdateStatementCount(4);
        verify(hibernateStatementAssertUtils, description("assert method is called")).assertInsertStatementCount(1);
        verify(hibernateStatementAssertUtils, description("assert method is called")).assertDeleteStatementCount(2);
    }
}
