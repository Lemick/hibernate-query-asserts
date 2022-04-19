# Hibernate SQL query count assertions for Spring

Hibernate is a powerful ORM, but you need to have control over the executed SQL queries to avoid **huge performance problems** (N+1 selects, silent updates, batch insert not working...) 

You can enable SQL query logging, this is a great help in dev, but not in production. This tool helps you to count the **executed SQL queries by Hibernate in your integration tests**.

It consists of just an Hibernate SQL inspector service and a Spring Test Listener that controls it (no proxy around the Datasource).

The assertion will work seamlessly whether you're testing Spring repositories or doing HTTP integration tests.

## Example

* You just have to add the @AssertHibernateSQLCount annotation to your test and it will verify the SQL statements (SELECT, UPDATE, INSERT, DELETE) count at the end of the test :


        @Test
        @Transactional
        @AssertHibernateSQLCount(inserts = 6)
        void create_two_blog_posts() {
            BlogPost post_1 = new BlogPost("Blog post 1");
            post_1.addComment(new PostComment("Good article"));
            post_1.addComment(new PostComment("Very interesting"));
            blogPostRepository.save(post_1);
    
            BlogPost post_2 = new BlogPost("Blog post 2");
            post_2.addComment(new PostComment("Nice"));
            post_2.addComment(new PostComment("So cool, thanks"));
            blogPostRepository.save(post_2);
        }

    If the actual count is different, an exception is thrown with the executed statements:
    
        com.lemick.assertions.HibernateStatementCountException: 
        Expected 5 INSERT but got 6:
             => '/* insert com.lemick.testdatasourceproxy.entity.BlogPost */ insert into blog_post (id, title) values (default, ?)'
             => '/* insert com.lemick.testdatasourceproxy.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
             => '/* insert com.lemick.testdatasourceproxy.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
             => '/* insert com.lemick.testdatasourceproxy.entity.BlogPost */ insert into blog_post (id, title) values (default, ?)'
             => '/* insert com.lemick.testdatasourceproxy.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
             => '/* insert com.lemick.testdatasourceproxy.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
    
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
