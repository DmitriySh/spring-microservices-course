Books library
=======
Use `Spring Boot 2.x` and `Spring Data MongoDB 2.x`.<br/>
`Spring Data MongoDB` provides repository support for MongoDB. It makes the easier development of applications
 that need to access to NoSQL document database.


## Description
 * The program stores information about books in the Library
 * The program runs in a terminal and use:
   * independent `MongoDB` instance on localhost `(default: mongodb://localhost:27017/library)`
   * customize instance on startup `(-Dspring.data.mongodb.uri=mongodb://localhost:27017/library)`
   * database: library
   * collections: book, author, genre
 * Services covered by unit tests


## Documentation
`Spring Data` for MongoDB is part of the umbrella `Spring Data` project which aims to provide a familiar 
and consistent Spring-based programming model for new datastores while retaining store-specific features and capabilities.

 - [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html) supports in-memory embedded MongoDB for the tests: 
   - it provides [de.flapdoodle.embed](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) project a platform neutral way for running MongoDB in unittests
   - stable and a development version support for Linux, Windows and macOS (2.x and 3.x)
   - you could use autoconfiguration way without or customize `MongodStarter` instance on runtime via source code or configuration file
   - customization includes: logging, mongodb version, random/static port and uri, startup options, start/stop process and others
   - define your own `IMongodConfig` and `IRuntimeConfig` beans to take control of the Mongo instance’s
   - to use a randomly allocated free port, use a value of 0

 - [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html) 
 provides a [MongoTemplate](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/core/MongoTemplate.html)
 class that is very similar in its design to Spring’s [JdbcTemplate](https://docs.spring.io/spring-framework/docs/5.0.x/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html)

 - annotation [@DataMongoTest](https://docs.spring.io/spring-boot/docs/2.0.x/api/org/springframework/boot/test/autoconfigure/data/mongo/DataMongoTest.html) does:
   - configures an in-memory embedded MongoDB, 
   - configures a [MongoTemplate](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/core/MongoTemplate.html), 
   - scans for [@Document](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/core/mapping/Document.html) classes
   - configures [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html) repositories like for JPA
 - If you prefer to run tests against a real MongoDB server, you should exclude the embedded MongoDB auto-configuration

 - [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html)
 creates `Repository` implementations like in [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/2.0.x/reference/html)
   - this is enabled by default or you need to use [@EnableMongoRepositories](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/repository/config/EnableMongoRepositories.html) annotation to enable explicitly
   - `Repository` interface is the central interface in the `Spring Data` repository abstraction but you could use [MongoRepository](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/repository/MongoRepository.html) 
   for better technology-specific abstractions of MongoDB
   - if you don't want to extend `Spring Data` interfaces, you can also annotate your repository interface with [@RepositoryDefinition](https://docs.spring.io/spring-data/commons/docs/2.0.x/api/org/springframework/data/repository/RepositoryDefinition.html)
   - custom intermediate repository interfaces should be annotated with [@NoRepositoryBean](https://docs.spring.io/spring-data/commons/docs/2.0.x/api/org/springframework/data/repository/NoRepositoryBean.html)

 - Spring supports custom interfaces with implementation classes instead of standard [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html) repositories
   - classes need to follow the naming convention: interface name + 'Impl' at the end by default
   - use [MongoTemplate](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/core/MongoTemplate.html) in the implementation class to performing operations in MongoDB
   - [MongoTemplate](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/core/MongoTemplate.html) is thread-safe and offers convenience operations to CRUD and provides a mapping between domain objects and MongoDB documents

 - MongoDB requires that you have an `_id` field for all documents
 - If you don't provide one, the driver assigns an `ObjectId` with a generated value
   - `ObjectId` is a 12-byte value consists of:
     - 4-byte value representing the seconds since the Unix epoch
     - 3-byte machine identifier
     - 2-byte process id
     - 3-byte counter, starting with a random value
   - `_id` field could be also String or BigInteger
   - `_id` field is generated by the Java driver if it hadn't been defined before
 - A property or field annotated with [@Id](org.springframework.data.annotation.Id) maps to the real `_id` field
 - A property or field without an annotation but named `id` maps to the real `_id` field
  
 - Spring Framework supports JSR 305 annotations:
   - define [@NonNullApi](https://docs.spring.io/spring-framework/docs/5.0.x/javadoc-api/org/springframework/lang/NonNullApi.html) 
   in `package-info.java` to enable runtime checking of nullability constraints for query methods
   - use [@NonNull](https://docs.spring.io/spring-framework/docs/5.0.x/javadoc-api/org/springframework/lang/NonNull.html) 
   or [@Nullable](https://docs.spring.io/spring-framework/docs/5.0.x/javadoc-api/org/springframework/lang/Nullable.html) to concretisize `null` checks

 - How is the `Spring Framework` can separate implementations between [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/2.0.x/reference/html) 
 and [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html)?
   - extends the module-specific repository like [JpaRepository](https://docs.spring.io/spring-data/data-jpa/docs/2.0.x/api/org/springframework/data/jpa/repository/JpaRepository.html) 
   or [MongoRepository](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/repository/MongoRepository.html)
   - use separate annotations for domain objects like [@Entity](https://docs.oracle.com/javaee/7/api/javax/persistence/Entity.html) 
   or [@Document](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/core/mapping/Document.html) and don't mix their together
   - define the scan packages in field `basePackages` for [@EnableJpaRepositories](https://docs.spring.io/spring-data/data-jpa/docs/2.0.x/api/org/springframework/data/jpa/repository/config/EnableJpaRepositories.html) 
   or [@EnableMongoRepositories](https://docs.spring.io/spring-data/data-mongo/docs/2.0.x/api/org/springframework/data/mongodb/repository/config/EnableMongoRepositories.html)
   - other cases leads to undefined behavior

 -  The query builder mechanism built into Spring Data repository infrastructure is useful for building queries over entities of the repository:
    - the algorithm checks the domain class for a property with the fool big name or tries to splits up the source at the camel case parts into a head and a tail and tries to find the corresponding small properties
    - to resolve the name ambiguity you can use underscore `_` inside your method name `List<Person> findByAddress_ZipCode(ZipCode zipCode)`
    - numeric value can be appended to `top` or `first` to specify the maximum result size to be returned `List<User> findTop10ByLastname(String lastname, Pageable pageable)`
 - use [@Query](org.springframework.data.mongodb.repository.Query) annotation to your repository query methods to specify a MongoDB JSON query instead of having the query be derived from the method name
 - Query by Example (QBE) is a user-friendly querying technique with a simple interface allow dynamic query creation


 - MongoDB doesn't have relationships beetween domain objects like in JPA (`@OneToMney`, `@OnetoOne` and others) but you can use 
 a [@DBRef](https://docs.spring.io/spring-data/mongodb/docs/2.0.x/api/org/springframework/data/mongodb/core/mapping/DBRef.html) 
 to refer to other document.
 - The mapping framework doesn't handle cascading saves and delete


Spring Data MongoDB: https://docs.spring.io/spring-data/mongodb/docs/2.0.x/reference/html

Spring Boot MongoDB: https://docs.spring.io/spring-boot/docs/2.0.x/reference/htmlsingle/#boot-features-mongodb


## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
...
BUILD SUCCESSFUL in 43s
40 actionable tasks: 40 executed
```

  *  Run server: 
```sh
spring-microservices-course$ ./08-spring-data-mongodb/build/libs/08-spring-data-mongodb-all-0.1.jar

 _       _____  ______   ______   ______   ______  __    _
| |       | |  | |  | \ | |  | \ | |  | | | |  | \ \ \  | |
| |   _   | |  | |--| < | |__| | | |__| | | |__| |  \_\_| |
|_|__|_| _|_|_ |_|__|_/ |_|  \_\ |_|  |_| |_|  \_\  ____|_|
Dmitriy Shishmakov | Spring Boot

2018-09-09 11:07:31.761  INFO 83375 --- [           main] ru.shishmakov.Main                       : Starting Main on shishmakov.local with PID 83375 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/08-spring-data-mongodb/build/libs/08-spring-data-mongodb-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/08-spring-data-mongodb/build/libs)
2018-09-09 11:07:31.766  INFO 83375 --- [           main] ru.shishmakov.Main                       : No active profile set, falling back to default profiles: default
2018-09-09 11:07:31.814  INFO 83375 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@3ac42916: startup date [Sun Sep 09 11:07:31 MSK 2018]; root of context hierarchy
2018-09-09 11:07:32.805  INFO 83375 --- [           main] org.mongodb.driver.cluster               : Cluster created with settings {hosts=[localhost:27017], mode=SINGLE, requiredClusterType=UNKNOWN, serverSelectionTimeout='30000 ms', maxWaitQueueSize=500}
2018-09-09 11:07:32.886  INFO 83375 --- [localhost:27017] org.mongodb.driver.connection            : Opened connection [connectionId{localValue:1, serverValue:5}] to localhost:27017
2018-09-09 11:07:32.891  INFO 83375 --- [localhost:27017] org.mongodb.driver.cluster               : Monitor thread successfully connected to server with description ServerDescription{address=localhost:27017, type=STANDALONE, state=CONNECTED, ok=true, version=ServerVersion{versionList=[3, 6, 2]}, minWireVersion=0, maxWireVersion=6, maxDocumentSize=16777216, logicalSessionTimeoutMinutes=30, roundTripTimeNanos=2937655}
2018-09-09 11:07:33.144  INFO 83375 --- [           main] org.mongodb.driver.connection            : Opened connection [connectionId{localValue:2, serverValue:6}] to localhost:27017

	Welcome to demo Library!

2018-09-09 11:07:33.832  INFO 83375 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-09-09 11:07:33.844  INFO 83375 --- [           main] ru.shishmakov.Main                       : Started Main in 2.467 seconds (JVM running for 2.986)
2018-09-09 11:07:33.845  INFO 83375 --- [           main] ru.shishmakov.config.DataInitializer    : clean old data...
2018-09-09 11:07:33.865  INFO 83375 --- [           main] ru.shishmakov.config.DataInitializer    : init default data...
2018-09-09 11:07:34.101  INFO 83375 --- [           main] ru.shishmakov.config.DataInitializer    : authors: [Author(id=5b94d4c595c0dc45af6d11d3, fullname=author 1), Author(id=5b94d4c595c0dc45af6d11d4, fullname=author 2), Author(id=5b94d4c595c0dc45af6d11d5, fullname=author 3), Author(id=5b94d4c595c0dc45af6d11d6, fullname=author 4)]
2018-09-09 11:07:34.104  INFO 83375 --- [           main] ru.shishmakov.config.DataInitializer    : genres: [Genre(id=5b94d4c595c0dc45af6d11d7, name=genre 1), Genre(id=5b94d4c595c0dc45af6d11d8, name=genre 2), Genre(id=5b94d4c595c0dc45af6d11d9, name=genre 3)]
2018-09-09 11:07:34.109  INFO 83375 --- [           main] ru.shishmakov.config.DataInitializer    : books: [Book(id=5b94d4c695c0dc45af6d11e0, title=book 1, isbn=0-395-08254-1), Book(id=5b94d4c695c0dc45af6d11e1, title=book 2, isbn=0-395-08254-2), Book(id=5b94d4c695c0dc45af6d11e2, title=book 3, isbn=0-395-08254-3), Book(id=5b94d4c695c0dc45af6d11e3, title=book 4, isbn=0-395-08254-4)]
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


shell:>
```


## Stop

 * The program is terminated at the end of quiz, by command `exit` or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
...
shell:>exit

	Goodbye! =)

2018-09-09 11:08:25.604  INFO 83375 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@3ac42916: startup date [Sun Sep 09 11:07:31 MSK 2018]; root of context hierarchy
2018-09-09 11:08:25.607  INFO 83375 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
2018-09-09 11:08:25.611  INFO 83375 --- [       Thread-2] org.mongodb.driver.connection            : Closed connection [connectionId{localValue:2, serverValue:6}] to localhost:27017 because the pool has been closed.
```
