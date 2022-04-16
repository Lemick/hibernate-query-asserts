# Hibernate SQL query count assertions for Spring

Hibernate is a powerful ORM, but you need to have control over the executed SQL queries to avoid huge performance problems (N+1 selects, silent updates, batch insert not working, etc...)

You can enable SQL query logging, this is a great help in dev, but not in production. 

This tool helps you to count the real executed queries by Hibernate in your integration tests.

It consists of just an Hibernate SQL inspector service and a Spring Test Listener that controls it (no proxy around the Datasource)

## Example

* You just have to add the @AssertSQLStatementCount annotation to your test and it will verify the SQL statements count at the end of the test:


        @Test
        @Transactional
        @AssertSQLStatementCount(deletes = 1, inserts = 2)
        void remove_one_entity_and_create_two() {
            blogPostRepository.deleteById(1L);
        
            BlogPost post_2 = new BlogPost("Post title 2");
            blogPostRepository.save(post_2);

            BlogPost post_3 = new BlogPost("Post title 3");
            blogPostRepository.save(post_3);
        }

    If the actual count is different, an exception is thrown:
    
        com.lemick.assertions.HibernateStatementCountException: 
        Expected 2 INSERT but was 1
        Expected 1 DELETE but was 0

* You can also use the static methods in your test method, but it's more complex because Hibernate [will try to delay the flush](https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/chapters/flushing/Flushing.html) of your entities states at the end of your application transaction, here we call flush manually:

        @Test
        @Transactional
        void create_two_entities() {
            BlogPost post_1 = new BlogPost("Post title 1");
            blogPostRepository.save(post_1);
            entityManager.flush();
            assertInsertStatementCount(1);
    
            BlogPost post_2 = new BlogPost("Post title 2");
            blogPostRepository.save(post_2);
            entityManager.flush();
            assertInsertStatementCount(2);
        }
    
## How to integrate
1. Register the integration with Hibernate, you just need to add this key in your configuration (here for yml):

        spring:
            jpa:
                properties:
                    hibernate.session_factory.statement_inspector: com.lemick.integration.hibernate.HibernateStatementCountInspector

2. Register the Spring TestListener that will launch the SQL inspection if the annotation is present:

    * By adding the listener on each of your integration test: 

    	    @SpringBootTest
            @TestExecutionListeners(
    	        listeners = HibernateStatementCountTestListener.class, 
    	        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
    	    )
    	    class MySpringIntegrationTest {
    	       ...
    	    }
	
    * **OR** by adding a **META-INF/spring.factories** file that contains the definition, that will register the listener for all your tests:

	      org.springframework.test.context.TestExecutionListener=com.lemick.integration.spring.HibernateStatementCountTestListener

