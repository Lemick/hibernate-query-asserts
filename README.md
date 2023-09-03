[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Lemick/hibernate-spring-sql-query-count.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Lemick/hibernate-spring-sql-query-count/context:java)
# Hibernate SQL Query Assertions for Spring

Hibernate is a powerful ORM, but you need to have control over the executed SQL queries to avoid **huge performance problems** (N+1 selects, batch insert not working...) 

You can enable SQL query logging, this is a great help in dev, but not in production. This tool helps you to count the **executed SQL queries by Hibernate in your integration tests, it can assert L2C statistics too**.

It consists of just a Hibernate SQL inspector service and a Spring Test Listener that controls it (no proxy around the Datasource).

The assertion will work seamlessly whether you're testing Spring repositories or doing HTTP integration tests.

## Examples

A full-working demo of the examples below [is available here](https://github.com/Lemick/demo-hibernate-query-utils)

*Tested versions*: Hibernate 5 & 6 

### Assert SQL statements

You just have to add the `@AssertHibernateSQLCount` annotation to your test and it will verify the SQL statements (SELECT, UPDATE, INSERT, DELETE) count at the end of the test :

```java
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
```
If the actual count is different, an exception is thrown with the executed statements:
```   
        com.mickaelb.assertions.HibernateAssertCountException: 
        Expected 5 INSERT but got 6:
             => '/* insert com.lemick.demo.entity.BlogPost */ insert into blog_post (id, title) values (default, ?)'
             => '/* insert com.lemick.demo.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
             => '/* insert com.lemick.demo.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
             => '/* insert com.lemick.demo.entity.BlogPost */ insert into blog_post (id, title) values (default, ?)'
             => '/* insert com.lemick.demo.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
             => '/* insert com.lemick.demo.entity.PostComment */ insert into post_comment (id, blog_post_id, content) values (default, ?, ?)'
```
### Assert L2C statistics

It supports assertions on Hibernate level two cache statistics, useful for checking that your entities are cached correctly and that they will stay forever:
```java
    @Test
    @AssertHibernateL2CCount(misses = 1, puts = 1, hits = 1)
    void create_one_post_and_read_it() {
        doInTransaction(() -> {
            BlogPost post_1 = new BlogPost("Blog post 1");
            blogPostRepository.save(post_1);
        });

        doInTransaction(() -> {
            blogPostRepository.findById(1L); // 1 MISS + 1 PUT
        });

        doInTransaction(() -> {
            blogPostRepository.findById(1L); // 1 HIT
        });
    }
```
## How to integrate
1. Import the dependency
	```xml
	<dependency>
		<groupId>com.mickaelb</groupId>
		<artifactId>hibernate-query-asserts</artifactId>
		<version>2.0.0</version>
	</dependency>
	```
2. Register the integration with Hibernate, you just need to add this key in your configuration (here for yml):

        spring:
            jpa:
                properties:
                    hibernate.session_factory.statement_inspector: com.mickaelb.integration.hibernate.HibernateStatementInspector

3. Register the Spring TestListener that will launch the SQL inspection if the annotation is present:

    * By adding the listener on each of your integration test: 
	```java
    @SpringBootTest
    @TestExecutionListeners(
		listeners = HibernateAssertTestListener.class, 
		mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
    )
    class MySpringIntegrationTest {
       ...
    }
	```

    * **OR** by adding a **META-INF/spring.factories** file that contains the definition, that will register the listener for all your tests:
	```
	 org.springframework.test.context.TestExecutionListener=com.mickaelb.integration.spring.HibernateAssertTestListener
	```
