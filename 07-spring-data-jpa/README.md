Books library
=======
Use `Spring Boot 2.x` and `Spring Data JPA 2.x`.<br/>
`Spring Data JPA` provides repository support for the Java Persistence API (JPA). It makes the easier development of applications
 that need to access JPA data sources.


## Description
 * The program stores information about books in the Library
 * The program runs in a terminal and use:
   * `H2` in-memory database 
   * tables: book, author, genre and books comments
 * Services covered by unit tests


## Documentation
 * `Spring Data` is an umbrella of projects. The most of `Spring Data JPA` modules carry different major and minor version numbers. 
 The easiest way to find compatible versions is to rely on the `Spring Data Release Train BOM`.
 * `Spring Data JPA` repositories are default Spring beans. They are singleton scoped and eagerly initialized. 
 During startup, they already interact with the JPA [EntityManager](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/EntityManager.html) 
 for verification and metadata analysis purposes
 * `Spring Data JPA` adds a repository abstraction is to significantly reduce the amount of boilerplate code 
 required to implement data access layers for various persistence stores. 
 [Repository](https://docs.spring.io/spring-data/data-commons/docs/2.1.x/api/org/springframework/data/repository/Repository.html) 
 is a marker interface, for example, the [CrudRepository](https://docs.spring.io/spring-data/data-commons/docs/2.1.x/api/org/springframework/data/repository/CrudRepository.html) 
 is a subinterface and provides sophisticated CRUD functionality for the anyone entity class that is being managed

 * How to use Spring Data JPA:
   * [@EnableJpaRepositories](https://docs.spring.io/spring-data/jpa/docs/2.1.x/api/org/springframework/data/jpa/repository/config/EnableJpaRepositories.html) 
   annotated on Java configuration class is enable repositories; `basePackages` defines the starting points for scanning for repository interface definitions
   * define a custom domain class-specific repository interface (extend [Repository](https://docs.spring.io/spring-data/data-commons/docs/2.1.x/api/org/springframework/data/repository/Repository.html)) 
   and be typed to the Domain class and an ID type:
     * mark your interface as [@RepositoryDefinition](https://docs.spring.io/spring-data/commons/docs/2.1.x/api/org/springframework/data/repository/RepositoryDefinition.html) 
     or [@Repository](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/stereotype/Repository.html) 
     if you don't want to extend Spring Data interfaces;
     * mark your interface as [@NoRepositoryBean](https://docs.spring.io/spring-data/commons/docs/2.1.x/api/org/springframework/data/repository/NoRepositoryBean.html) 
     if this is an intermediate repository interface (like [CrudRepository](https://docs.spring.io/spring-data/data-commons/docs/2.1.x/api/org/springframework/data/repository/CrudRepository.html) or others) 
     and Spring shouldn't create instance at runtime;

 * `Spring Data JPA` provides implementations in runtime for each custom target repository, for instance, 
 class [SimpleJpaRepository](https://docs.spring.io/spring-data/jpa/docs/2.1.x/api/org/springframework/data/jpa/repository/support/SimpleJpaRepository.html) 
 is a default implementation of the interface [CrudRepository](https://docs.spring.io/spring-data/commons/docs/2.1.x/api/org/springframework/data/repository/CrudRepository.html)
 * The query builder mechanism could build query from name of method (example: `findByLastnameOrderByFirstnameAsc` = `select * from Person p where p.lastname = :lastname order by b.firstname ASC`)
 * The actual result of parsing the method to query depends on the persistence store for which you create the query
 * To resolve ambiguity in names of query you can use `_` inside your method name to manually define traversal points
 * In any time you could write JPQL query in [@Query](https://docs.spring.io/spring-data/jpa/docs/2.1.x/api/org/springframework/data/jpa/repository/Query.html)
  annotation and forget the method rules writing for query builder mechanism

 * Repository queries can be run asynchronously: use [@Async](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/scheduling/annotation/Async.html) 
 and [Future<T>](https://docs.oracle.com/javase/10/docs/api/java/util/concurrent/Future.html) as a result for the actual query, 
 execution occurs in a task that has been submitted to a `Spring TaskExecutor`
 * `Spring Data JPA` supports open/closed interfaces for `projections` if you want to retrieve easily 
 to read part of entity data instead of `List<Object[]>`
 * The query execution engine creates proxy instances of that interface at runtime for each element returned and forwards 
 calls to the exposed methods to the target object
```
interface NamesOnly {
  String getFirstname();
  String getLastname();
}
```


Spring Data JPA: https://docs.spring.io/spring-data/jpa/docs/2.1.x/reference/html


## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
                             
BUILD SUCCESSFUL in 43s
42 actionable tasks: 42 executed
```

  *  Run server: 
```sh
spring-microservices-course$ ./07-spring-data-jpa/build/libs/07-spring-data-jpa-all-0.1.jar

 _       _____  ______   ______   ______   ______  __    _
| |       | |  | |  | \ | |  | \ | |  | | | |  | \ \ \  | |
| |   _   | |  | |--| < | |__| | | |__| | | |__| |  \_\_| |
|_|__|_| _|_|_ |_|__|_/ |_|  \_\ |_|  |_| |_|  \_\  ____|_|
Dmitriy Shishmakov | Spring Boot

2018-08-26 18:09:51.881  INFO 52234 --- [           main] ru.shishmakov.Main                       : Starting Main on shishmakov.local with PID 52234 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/07-spring-data-jpa/build/libs/07-spring-data-jpa-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/07-spring-data-jpa/build/libs)
2018-08-26 18:09:51.887  INFO 52234 --- [           main] ru.shishmakov.Main                       : No active profile set, falling back to default profiles: default
2018-08-26 18:09:51.959  INFO 52234 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@60215eee: startup date [Sun Aug 26 18:09:51 MSK 2018]; root of context hierarchy
2018-08-26 18:09:52.962  INFO 52234 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2018-08-26 18:09:53.144  INFO 52234 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2018-08-26 18:09:53.149  INFO 52234 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executing SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/07-spring-data-jpa/build/libs/07-spring-data-jpa-all-0.1.jar!/BOOT-INF/classes!/schema.sql]
2018-08-26 18:09:53.186  INFO 52234 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executed SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/07-spring-data-jpa/build/libs/07-spring-data-jpa-all-0.1.jar!/BOOT-INF/classes!/schema.sql] in 37 ms.
2018-08-26 18:09:53.191  INFO 52234 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executing SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/07-spring-data-jpa/build/libs/07-spring-data-jpa-all-0.1.jar!/BOOT-INF/classes!/data.sql]
2018-08-26 18:09:53.201  INFO 52234 --- [           main] o.s.jdbc.datasource.init.ScriptUtils     : Executed SQL script from URL [jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/07-spring-data-jpa/build/libs/07-spring-data-jpa-all-0.1.jar!/BOOT-INF/classes!/data.sql] in 9 ms.
2018-08-26 18:09:53.359  INFO 52234 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2018-08-26 18:09:53.380  INFO 52234 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
2018-08-26 18:09:53.492  INFO 52234 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate Core {5.2.17.Final}
2018-08-26 18:09:53.496  INFO 52234 --- [           main] org.hibernate.cfg.Environment            : HHH000206: hibernate.properties not found
2018-08-26 18:09:53.567  INFO 52234 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
2018-08-26 18:09:53.817  INFO 52234 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
2018-08-26 18:09:54.477  INFO 52234 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2018-08-26 18:09:54.788  INFO 52234 --- [           main] o.h.h.i.QueryTranslatorFactoryInitiator  : HHH000397: Using ASTQueryTranslatorFactory

	Welcome to demo Library!

2018-08-26 18:09:55.620  INFO 52234 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-08-26 18:09:55.621  INFO 52234 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Bean with name 'dataSource' has been autodetected for JMX exposure
2018-08-26 18:09:55.625  INFO 52234 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
2018-08-26 18:09:55.637  INFO 52234 --- [           main] ru.shishmakov.Main                       : Started Main in 4.146 seconds (JVM running for 4.666)
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

2018-08-26 18:11:21.459  INFO 52234 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@60215eee: startup date [Sun Aug 26 18:09:51 MSK 2018]; root of context hierarchy
2018-08-26 18:11:21.465  INFO 52234 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
2018-08-26 18:11:21.466  INFO 52234 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans
2018-08-26 18:11:21.486  INFO 52234 --- [       Thread-2] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2018-08-26 18:11:21.487  INFO 52234 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2018-08-26 18:11:21.489  INFO 52234 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```
