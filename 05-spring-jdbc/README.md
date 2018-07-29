Library of books
=======
Use `Spring Boot JDBC 2.x`, `Spring Shell 2.x`.<br/>
The layer of data storage uses to safely persist data for a long time. The most common layer is RDBMS.


## Description
 * The program stores information about books in the Library
 * The program runs in a terminal and use:
   * `H2` in-memory database 
   * tables: book, author and genre
 * Services covered by unit tests


## Documentation
[DAO support](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/data-access.html#dao)
 * Data Access Object (DAO) is aimed at making it easy to work with data access technologies like JDBC, Hibernate or JPA in a consistent way.
 * Use the `@Repository` annotation is the best way 
   * to guarantee that your DAOs or repositories provide exception translation from most persistence exceptions (for instance SQLException) to its own unchecked exception class hierarchy with the DataAccessException as the root exception
   * the component scanning support to find and configure your DAOs and repositories without having to provide XML configuration
   * access to a persistence resource, depending on the persistence technology used: JDBC-based to a `DataSource`, JPA-based to an `EntityManager`

[Data access with JDBC](https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/data-access.html#jdbc)
 * `Spring JdbcTemplate` is a powerful mechanism to connect to the database and execute SQL queries. It internally uses JDBC API, but eliminates a lot of problems:
   * handle resources before and after executing the query, create and close connections, statements, resultsets and etc,
   * perform exception handling code on the database logic,
   * handle transaction,
   * repetition of all the same boilerplate code from one to another database logic

 * [JdbcTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html) is the central class to handle JDBC operations:
   * holds a reference to a `DataSource`
   * executes SQL queries or updates
   * simplifies use of JDBC and avoids common errors
   * add the wrapper class [NamedParameterJdbcTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/jdbc/core/namedparam/NamedParameterJdbcTemplate.html) to provide named parameters instead of the traditional JDBC `?` placeholders
   * threadsafe once configured

* [RowMapper](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/jdbc/core/RowMapper.html) interface that is used to map [ResultSet](https://docs.oracle.com/javase/10/docs/api/java/sql/ResultSet.html) with a Java object
* Provides support for creating embedded databases using Java database engines ([HSQLDB](http://hsqldb.org), [H2](http://www.h2database.com/html/main.html), [Derby](http://db.apache.org/derby/))
* `Spring Boot` finds resources `schema.sql`, `data.sql` and `test-data.sql` in the root of the classpath and initialize db during startup
* [TransactionTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/support/TransactionTemplate.html) class simplifies programmatic transaction demarcation and transaction exception handling.

Main site of `Spring Data JDBC`: https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/data-access.html


## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
                             
BUILD SUCCESSFUL in 17s
30 actionable tasks: 30 executed
```

  *  Run server: 
```sh
spring-microservices-course$ ./05-spring-jdbc/build/libs/05-spring-jdbc-all-0.1.jar                 
 _       _____  ______   ______   ______   ______  __    _
| |       | |  | |  | \ | |  | \ | |  | | | |  | \ \ \  | |
| |   _   | |  | |--| < | |__| | | |__| | | |__| |  \_\_| |
|_|__|_| _|_|_ |_|__|_/ |_|  \_\ |_|  |_| |_|  \_\  ____|_|
Dmitriy Shishmakov | Spring Boot

16:22:15.873 [main] INFO  r.s.Main - Starting Main on shishmakov.local with PID 40307 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/05-spring-jdbc/build/libs/05-spring-jdbc-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/05-spring-jdbc/build/libs)
16:22:15.879 [main] DEBUG r.s.Main - Running with Spring Boot v2.0.3.RELEASE, Spring v5.0.7.RELEASE
16:22:15.880 [main] INFO  r.s.Main - No active profile set, falling back to default profiles: default
16:22:15.941 [main] INFO  o.s.c.a.AnnotationConfigApplicationContext - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@768debd: startup date [Sun Jul 29 16:22:15 MSK 2018]; root of context hierarchy
16:22:16.749 [main] INFO  c.z.h.HikariDataSource - HikariPool-1 - Starting...
16:22:16.906 [main] INFO  c.z.h.HikariDataSource - HikariPool-1 - Start completed.
16:22:16.911 [main] INFO  o.s.j.d.i.ScriptUtils - Executing SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/05-spring-jdbc/build/libs/05-spring-jdbc-all-0.1.jar!/BOOT-INF/classes!/schema.sql]
16:22:16.944 [main] INFO  o.s.j.d.i.ScriptUtils - Executed SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/05-spring-jdbc/build/libs/05-spring-jdbc-all-0.1.jar!/BOOT-INF/classes!/schema.sql] in 33 ms.
16:22:16.951 [main] INFO  o.s.j.d.i.ScriptUtils - Executing SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/05-spring-jdbc/build/libs/05-spring-jdbc-all-0.1.jar!/BOOT-INF/classes!/data.sql]
16:22:16.958 [main] INFO  o.s.j.d.i.ScriptUtils - Executed SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/05-spring-jdbc/build/libs/05-spring-jdbc-all-0.1.jar!/BOOT-INF/classes!/data.sql] in 7 ms.

	Welcome to demo Library!

16:22:17.622 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Registering beans for JMX exposure on startup
16:22:17.622 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Bean with name 'dataSource' has been autodetected for JMX exposure
16:22:17.626 [main] INFO  o.s.j.e.a.AnnotationMBeanExporter - Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
16:22:17.636 [main] INFO  r.s.Main - Started Main in 2.111 seconds (JVM running for 2.698)
shell:>help
AVAILABLE COMMANDS

Built-In Commands
     clear: Clear the shell screen.
     help: Display help about available commands.
     history: Display or save the history of previously run commands
     stacktrace: Display the full stacktrace of the last error.

Library
     create-book: Create new book.
     delete-book: Delete the book.
     exit, quit: Exit the library.
     get-authors: Get all authors.
     get-book-authors: Get authors of the book.
     get-book-genres: Get genres of the book.
     get-books: Get all books.
     get-genres: Get all genres.
     h2: Run H2 database console.

shell:>
...
```


## Stop

 * The program is terminated at the end of quiz, by command `exit` or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
...
shell:>exit

	Goodbye! =)

16:25:10.671 [Thread-2] INFO  o.s.c.a.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@768debd: startup date [Sun Jul 29 16:22:15 MSK 2018]; root of context hierarchy
16:25:10.674 [Thread-2] INFO  o.s.j.e.a.AnnotationMBeanExporter - Unregistering JMX-exposed beans on shutdown
16:25:10.675 [Thread-2] INFO  o.s.j.e.a.AnnotationMBeanExporter - Unregistering JMX-exposed beans
16:25:10.675 [Thread-2] INFO  c.z.h.HikariDataSource - HikariPool-1 - Shutdown initiated...
16:25:10.677 [Thread-2] INFO  c.z.h.HikariDataSource - HikariPool-1 - Shutdown completed.
```
