package com.mickaelb.integration.spring;

import com.mickaelb.api.AssertHibernateL2CCount;
import com.mickaelb.api.AssertHibernateSQLCount;
import com.mickaelb.integration.hibernate.HibernateStatementStatistics;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestContext;

import java.util.List;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateAssertTestListenerTest {

    @InjectMocks
    HibernateAssertTestListener model;

    @Mock
    TestContext testContext;

    @Mock
    AssertTestListener assertTestListener;

    @BeforeEach
    public void init() {
        model.setTestListeners(List.of(assertTestListener));
    }

    @Test
    public void _beforeTestClass() {
        model.beforeTestClass(testContext);

        verify(assertTestListener, description("child method must be called")).beforeTestClass(testContext);
    }

    @Test
    public void _beforeTestMethod() {
        model.beforeTestMethod(testContext);

        verify(assertTestListener, description("child method must be called")).beforeTestMethod(testContext);
    }

    @Test
    public void _afterTestMethod() {
        model.afterTestMethod(testContext);

        verify(assertTestListener, description("child method must be called")).afterTestMethod(testContext);
    }
}
