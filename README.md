# Hibernate SQL query count assertions for Spring

Hibernate is a powerful ORM, but you need to have control over the executed SQL queries to avoid huge performance problems (N+1 select, silent updates, batch not working, etc...)

You can enable SQL query logging, this is a great help in dev, but this tool helps you to count the executed queries by Hibernate in your integrations test.

It consists of just an Hibernate SQL inspector service and a Spring Test Listener that reads it (so no proxy around the Datasource)

## Example

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

## How to integrate
- Register the SQL inspector service in the registry, you just need to add this key in your configuration (here for yml):


	spring:
      jpa:
        properties:
          hibernate.session_factory.statement_inspector: com.lemick.integration.hibernate.HibernateStatementCountInspector

- Register the Spring TestListener that will launch the SQL inspection is the annotation is present
