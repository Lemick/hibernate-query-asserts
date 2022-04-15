# Hibernate SQL query count assertions for Spring

Hibernate is a powerful ORM, but you need to have control over the executed SQL queries to avoid huge performance problems (N+1 selects, silent updates, batch insert not working, etc...)

You can enable SQL query logging, this is a great help in dev, but this tool helps you to count the executed queries by Hibernate in your integration tests.

It consists of just an Hibernate SQL inspector service and a Spring Test Listener that enable it (so no proxy around the Datasource)

## Example

You just have to add the @AssertSQLStatementCount annotation to your test and it will verify the SQL statements count at the end of the test:

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
    
## How to integrate
- Register the integration with Hibernate, you just need to add this key in your configuration (here for yml):

	  spring:
		  jpa:
		  	properties:
				hibernate.session_factory.statement_inspector: com.lemick.integration.hibernate.HibernateStatementCountInspector

- Register the Spring TestListener that will launch the SQL inspection if the annotation is present:

    * By adding the listener on each of your integration test: 

    	    @SpringBootTest
    	    @TestExecutionListeners(listeners = HibernateStatementCountTestListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
    	    class MySpringBootIntegrationTest {
    	       ...
    	    }
	
    * **OR** by adding a **META-INF/spring.factories** file that contains the definition, that will register the listener for all your tests:

	      org.springframework.test.context.TestExecutionListener=com.lemick.integration.spring.HibernateStatementCountTestListener

