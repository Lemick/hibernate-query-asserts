package com.mickaelb.api;

import com.mickaelb.integration.hibernate.HibernateStatementInspector;
import com.mickaelb.integration.spring.assertions.HibernateAssertCountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.mickaelb.api.StatementType.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QueryAssertionsTest {

    @BeforeEach
    public void setUp() {
        HibernateStatementInspector.getStatistics().resetStatistics();
    }

    @Test
    public void _assert_insert_count_success() {
        QueryAssertions.assertInsertCount(2, () -> {
            HibernateStatementInspector.getStatistics().notifyInsertStatement("INSERT INTO table test (id) VALUES (uuid())");
            HibernateStatementInspector.getStatistics().notifyInsertStatement("INSERT INTO table test (id) VALUES (uuid())");
        });

        QueryAssertions.assertInsertCount(1, () -> HibernateStatementInspector.getStatistics().notifyInsertStatement("INSERT INTO table test (id) VALUES (uuid())"));
    }

    @Test
    public void _assert_insert_count_failure() {
        Runnable insertRunnable = () -> {
            HibernateStatementInspector.getStatistics().notifyInsertStatement("INSERT INTO table test (id) VALUES (uuid())");
            HibernateStatementInspector.getStatistics().notifyInsertStatement("INSERT INTO table test (id) VALUES (uuid())");
        };

        assertThrows(HibernateAssertCountException.class, () -> QueryAssertions.assertInsertCount(3, insertRunnable));
    }

    @Test
    public void _assert_update_count_success() {
        QueryAssertions.assertUpdateCount(2, () -> {
            HibernateStatementInspector.getStatistics().notifyUpdateStatement("UPDATE table SET id=1");
            HibernateStatementInspector.getStatistics().notifyUpdateStatement("UPDATE table SET id=1");
        });

        QueryAssertions.assertUpdateCount(1, () -> {
            HibernateStatementInspector.getStatistics().notifyUpdateStatement("UPDATE table SET id=1");
        });
    }

    @Test
    public void _assert_update_count_failure() {
        Runnable updateRunnable = () -> {
            HibernateStatementInspector.getStatistics().notifyUpdateStatement("UPDATE table SET id=1");
        };

        assertThrows(HibernateAssertCountException.class, () -> QueryAssertions.assertUpdateCount(2, updateRunnable));
    }

    @Test
    public void _assert_select_count_success() {
        QueryAssertions.assertSelectCount(2, () -> {
            HibernateStatementInspector.getStatistics().notifySelectStatement("SELECT * FROM table");
            HibernateStatementInspector.getStatistics().notifySelectStatement("SELECT * FROM table");
        });

        QueryAssertions.assertSelectCount(1, () -> {
            HibernateStatementInspector.getStatistics().notifySelectStatement("SELECT * FROM table");
        });
    }

    @Test
    public void _assert_select_count_failure() {
        Runnable selectRunnable = () -> {
            HibernateStatementInspector.getStatistics().notifySelectStatement("SELECT * FROM table");
            HibernateStatementInspector.getStatistics().notifySelectStatement("SELECT * FROM table");
        };

        assertThrows(HibernateAssertCountException.class, () -> QueryAssertions.assertSelectCount(4, selectRunnable));
    }

    @Test
    public void _assert_delete_count_success() {
        QueryAssertions.assertDeleteCount(2, () -> {
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
        });

        QueryAssertions.assertDeleteCount(1, () -> {
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
        });
    }

    @Test
    public void _assert_delete_count_failure() {
        Runnable deleteRunnable = () -> {
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
        };

        assertThrows(HibernateAssertCountException.class, () -> QueryAssertions.assertDeleteCount(2, deleteRunnable));
    }

    @Test
    public void _assert_multiple_count_success() {
        QueryAssertions.assertStatementCount(Map.of(DELETE, 2, UPDATE, 1, SELECT, 0), () -> {
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
            HibernateStatementInspector.getStatistics().notifyUpdateStatement("UPDATE table SET id=1");
        });

        QueryAssertions.assertStatementCount(Map.of(DELETE, 2, SELECT, 1, INSERT, 0), () -> {
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
            HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
            HibernateStatementInspector.getStatistics().notifySelectStatement("SELECT * FROM table");
        });
    }

    @Test
    public void _assert_multiple_count_failure() {
        assertThrows(HibernateAssertCountException.class, () -> {
            QueryAssertions.assertStatementCount(Map.of(DELETE, 2, UPDATE, 1, SELECT, 0), () -> {
                HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
                HibernateStatementInspector.getStatistics().notifyDeleteStatement("DELETE FROM table WHERE 1=1");
            });
        });
    }
}
