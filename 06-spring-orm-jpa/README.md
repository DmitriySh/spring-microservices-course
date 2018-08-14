Books library
=======
Use `Spring Boot 2.x` and `Spring ORM`.<br/>
Persistence technology defines objects state (framework) and persists state (database) instead of manually work with the database layer.


## Description
 * The program stores information about books in the Library
 * The program runs in a terminal and use:
   * `H2` in-memory database 
   * tables: book, author, genre and books comments
 * Services covered by unit tests


## Documentation
 * [ORM](https://en.wikipedia.org/wiki/Object-relational_mapping)-based frameworks require a transaction in order to trigger the synchronization between the objects state (framework) and persist state (database). If you are using an ORM-based framework, you must use transactions
   * Spring-ORM is an umbrella module that covers persistence technologies [JPA](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/package-summary.html) and [Hibernate](http://hibernate.org/orm/). Spring provides integration classes for each of these technologies so that each technology can be used following Spring principles of configuration and smoothly integrates with Spring transaction management

 * Comprehensive transaction support is among the most compelling reasons to use the Spring Framework. It provides a consistent abstraction for transaction management that delivers the following benefits:
   * declarative transaction management,
   * simpler API than complex transaction APIs such as JTA,
   * consistent programming model across different transaction APIs: JTA, JPA, JDBC
   * solves threadsafe problems to create and bind a Session to the current thread transparently, by exposing a current Session through the Hibernate SessionFactory


 * Java EE developers have had two choices for transaction management: `global` and `local` transactions, both of which have profound limitations
   * global: should work in an application server environment
   * global: cumbersome API to use
   * global: needs to be sourced from JNDI
   * local: cannot work across multiple transactional resources (within a global JTA transaction)
   * local: programmatic transaction management
   * local: database transaction level
 * Spring don't need an application server to manage transactions: you write your code once, and it can benefit from different transaction management strategies in different environments


 * Spring Framework provides programmatic and declarative transaction supports:
   * two type of programmatic transaction management: class [TransactionTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/support/TransactionTemplate.html)
   and implementations of interface [PlatformTransactionManager](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html)
   * declarative transaction supports is enabled via AOP proxies and metadata (XML or annotation-based) to create a transactional proxy around the object

 * The interface [PlatformTransactionManager](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html):
   * it can be easily mocked or stubbed as necessary
   * it can be used programmatically (commit/rollback)
   * defined like any other bean in the IoC container
   * use unchecked exceptions ([TransactionException](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/TransactionException.html))
   and don't force to recover the state after transaction failures if a developer doesn't want to catch and handle them
   * only unchecked exceptions marks a transaction for rollback by default and checked exceptions are not
   * is similar to using the JTA UserTransaction API
 * [TransactionTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/support/TransactionTemplate.html)
 and [TransactionInterceptor](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/interceptor/TransactionInterceptor.html)
 delegate the actual transaction handling to a `PlatformTransactionManager` instance, which can be a [HibernateTransactionManager](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/orm/hibernate5/HibernateTransactionManager.html)
 (for a single Hibernate SessionFactory, using a ThreadLocal Session under the hood) or a [JtaTransactionManager](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/jta/JtaTransactionManager.html) (delegating to the JTA subsystem of the container)


 * `getTransaction(..)` method returns a [TransactionStatus](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/TransactionStatus.html) object,
 depending on a [TransactionDefinition](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/TransactionDefinition.html)
 parameter specifies transaction rules: Propagation, Isolation, Timeout, Read-only status
 * Spring’s highest level template handle resource creation and reuse, cleanup, optional transaction synchronization of the resources,
 exception mapping and can be focused purely on non-boilerplate persistence logic
 * Spring low-level approach is to use classes such as [DataSourceUtils](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/jdbc/datasource/DataSourceUtils.html) (for JDBC),
 [EntityManagerFactoryUtils](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/orm/jpa/EntityManagerFactoryUtils.html) (for JPA), 
 [SessionFactoryUtils](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/orm/hibernate5/SessionFactoryUtils.html) (for Hibernate)

 * Mapping JDBC to JPA:
   * `EntityManagerFactory` is a `DataSource`
   * `EntityManager` with `EntityTransaction` is a `Connection`
   * `@Entity` annotates a class represents the table in db
   * `@Column` (is optional) annotates a class field represents the row column in table

 * JPA mapping rules:
   * entity class should have at least one public/protected default constructor
   * not final class, access methods and fields
   * should have Serializable interface if the entity is going to be transferred by the network
   * should have public setter/getter methods and private entity fields

 * Entity states:
   * `New` is a new entity object that didn't connect with any EntityManager yet; use `persist()` to make that connection (like an `upsert` command)
   * `Managed` is an entity object connected with EntityManager already; use `refresh()` to fetch data from db
   * `Removed` is an entity object deleted from EntityManager by `remove()` method; use `persist()` to restore a deleted entity
   * `Detached` is an entity object after: end of transaction, close EntityManager, serialization/deserialization; use `merge()` to sync data with db
   * `Persisted` is a data available only in db and you need to use the method `find()` to fetch and map it to the managed entity object


Spring Transaction Management: https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/data-access.html#transaction
Spring ORM: https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/data-access.html#orm


## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
                             
BUILD SUCCESSFUL in 29s
36 actionable tasks: 36 executed
```

  *  Run server: 
```sh
spring-microservices-course$ ./06-spring-orm-jpa/build/libs/06-spring-orm-jpa-all-0.1.jar

 _       _____  ______   ______   ______   ______  __    _
| |       | |  | |  | \ | |  | \ | |  | | | |  | \ \ \  | |
| |   _   | |  | |--| < | |__| | | |__| | | |__| |  \_\_| |
|_|__|_| _|_|_ |_|__|_/ |_|  \_\ |_|  |_| |_|  \_\  ____|_|
Dmitriy Shishmakov | Spring Boot

2018-08-14 06:29:05.603  INFO 12980 --- [           main] ru.shishmakov.Main                       : Starting Main on shishmakov.local with PID 12980 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/06-spring-orm-jpa/build/libs/06-spring-orm-jpa-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/06-spring-orm-jpa/build/libs)
2018-08-14 06:29:05.609  INFO 12980 --- [           main] ru.shishmakov.Main                       : No active profile set, falling back to default profiles: default
2018-08-14 06:29:05.658  INFO 12980 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@60215eee: startup date [Tue Aug 14 06:29:05 MSK 2018]; root of context hierarchy
2018-08-14 06:29:06.581  INFO 12980 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2018-08-14 06:29:06.748  INFO 12980 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2018-08-14 06:29:06.752  INFO 12980 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executing SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/06-spring-orm-jpa/build/libs/06-spring-orm-jpa-all-0.1.jar!/BOOT-INF/classes!/schema.sql]
2018-08-14 06:29:06.795  INFO 12980 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executed SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/06-spring-orm-jpa/build/libs/06-spring-orm-jpa-all-0.1.jar!/BOOT-INF/classes!/schema.sql] in 43 ms.
2018-08-14 06:29:06.800  INFO 12980 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executing SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/06-spring-orm-jpa/build/libs/06-spring-orm-jpa-all-0.1.jar!/BOOT-INF/classes!/data.sql]
2018-08-14 06:29:06.815  INFO 12980 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executed SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/06-spring-orm-jpa/build/libs/06-spring-orm-jpa-all-0.1.jar!/BOOT-INF/classes!/data.sql] in 14 ms.
2018-08-14 06:29:06.958  INFO 12980 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2018-08-14 06:29:06.979  INFO 12980 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
2018-08-14 06:29:07.085  INFO 12980 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate Core {5.2.17.Final}
2018-08-14 06:29:07.089  INFO 12980 --- [           main] org.hibernate.cfg.Environment            : HHH000206: hibernate.properties not found
2018-08-14 06:29:07.150  INFO 12980 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
2018-08-14 06:29:07.390  INFO 12980 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
2018-08-14 06:29:08.025  INFO 12980 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'

	Welcome to demo Library!

2018-08-14 06:29:08.846  INFO 12980 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-08-14 06:29:08.847  INFO 12980 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Bean with name 'dataSource' has been autodetected for JMX exposure
2018-08-14 06:29:08.851  INFO 12980 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
2018-08-14 06:29:08.865  INFO 12980 --- [           main] ru.shishmakov.Main                       : Started Main in 3.822 seconds (JVM running for 4.341)
shell:>help
AVAILABLE COMMANDS

Built-In Commands
        clear: Clear the shell screen.
        help: Display help about available commands.
        history: Display or save the history of previously run commands
        stacktrace: Display the full stacktrace of the last error.

Library Shell
        create-book: Create new book.
        create-book-comment: Create new comment to book.
        delete-book: Delete the book.
        delete-book-comment: Delete the comment.
        exit, quit: Exit the library.
        get-authors: Get all authors.
        get-book-authors: Get authors of the book.
        get-book-comments: Get comments of the book.
        get-book-genres: Get genres of the book.
        get-books: Get all books.
        get-comments: Get all comments.
        get-genres: Get all genres.
        h2: Run H2 database console.


shell:>
```


## Stop

 * The program is terminated at the end of quiz, by command `exit` or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
...
shell:>exit

	Goodbye! =)

2018-08-14 06:30:58.694  INFO 12980 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@60215eee: startup date [Tue Aug 14 06:29:05 MSK 2018]; root of context hierarchy
2018-08-14 06:30:58.699  INFO 12980 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
2018-08-14 06:30:58.700  INFO 12980 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans
2018-08-14 06:30:58.700  INFO 12980 --- [       Thread-2] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2018-08-14 06:30:58.701  INFO 12980 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2018-08-14 06:30:58.736  INFO 12980 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```
