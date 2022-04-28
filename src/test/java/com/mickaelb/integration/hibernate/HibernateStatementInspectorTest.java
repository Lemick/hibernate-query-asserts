package com.mickaelb.integration.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateStatementInspectorTest {

    @InjectMocks
    HibernateStatementInspector model;

    @Mock
    HibernateStatementParser statementParser;

    @Test
    public void _inspect() {
        String actual = model.inspect("SELECT * FROM Post");

        assertEquals("SELECT * FROM Post", actual, "output is the same as the input");
        verify(statementParser, description("parser is called")).parseSqlStatement("SELECT * FROM Post", HibernateStatementInspector.getStatistics());
    }
}
