package com.lemick.integration.spring;

import com.lemick.api.AssertSQLStatementCount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestContext;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateStatementCountTestListenerTest {

    public class FakeClass {
        @AssertSQLStatementCount
        public void annotatedMethod() {

        }

        public void notAnnotatedMethod() {

        }
    }

    @InjectMocks
    HibernateStatementCountTestListener model;

    @Mock
    TestContext testContext;

    @Test // TODO bridge pour mock static
    public void _beforeTestMethod() throws NoSuchMethodException {
        when(testContext.getTestMethod()).thenReturn(FakeClass.class.getMethod("notAnnotatedMethod"));

        model.beforeTestMethod(testContext);
    }
}
