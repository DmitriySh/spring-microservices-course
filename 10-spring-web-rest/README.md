Books library
=======
Use `Spring Boot 2.x` and `Spring Web MVC 5.x` with `REST` support.<br/>
`REST` has quickly become the de-facto standard for building web services on the web because they’re easy to build and easy to consume. 
The Spring team provided first-class `REST` support starting with `Spring 3.0`. The `Spring MVC` framework resides pretty well with REST 
and provides the necessary API support to implement it seamlessly, with little effort.


## Description
 * The program is a headless server shows information about books in the Library by `RESTful API`
 * The program uses:
   * HTTP for communication with web server:
   * `H2` in-memory database 
   * table book
 * Services covered by unit tests


## Documentation
 * A `web server` uses HTTP protocol to transfer data:
   * it receives HTTP request
   * forwards the request to servlet container
   * return the dynamically generated result to the client (initiator of request)

 * A `servlet container` is using for:
   * supports the life cycle of servlets
   * servlets are written on Java and need to handle requests and dynamically generate the responses on the server side
   * support authentificaion, authorization and http sessions

 * The `Spring Framework` is an application framework and IoC container for the Java platform.
 * `Spring Boot` is not a Framework, it is Spring's [convention over configuration](https://en.wikipedia.org/wiki/Convention_over_configuration) solution for creating stand-alone, production-grade Spring-based applications
 * Convention over configuration it is a software design paradigm used by software frameworks that attempt to decrease complexity without losing flexibility

 - `REST` (REpresentational State Transfer) is an architectural style that defines a set of constraints to be used for creating a distributed application in the network. This is how web services interact
 - `REST` is not a framework, a protocol or communication format it is an architectural style or design pattern for APIs
 - `REST` was defined by Roy Fielding, a computer scientist. He presented the `REST` principles in his Ph.D. dissertation in 2000

 - The system supporting `REST` is a `RESTful`, each unit of information is URL with already defined standard declarations based on HTTP
 - HTTP commands are used for CRUD operations: GET (retrieve), PUT (update/modify), POST (create), DELETE (remove)
 - It means when a `RESTful API` is called, the server will use HTTP command and transfer to the client a representation state of the requested resource
 - The representation of the state can be in a JSON format and probably for most APIs this is indeed the case. It can also be in XML or HTML format
 
 * `RESTful API` should follow 6 constraints:
   * Uniform interface
     Interactions client-server should be defined by predefined rules
       1) each resource must be uniquely identified by a stable identifier. A `stable` identifier means that it doesn't change across interactions, 
       and it doesn't change even when the state of the resource changes
       2) the response the server returns include enough information to modify or delete the resource
       3) each request from the client should contain all the information the server needs to process its request, 
       and each response from the server should contain all the information the client needs in order to understand the response
       4) hypermedia. It is a data sent from the server to the client that contains information about what the client can do next–in other words, 
       what further requests it can make. In `REST`, servers should be sending only hypermedia to clients. HTML links are a type of hypermedia.
   * Client-server
     * communication between 2 separated items: `client` and `server`; 
     * the client and the server act independently, each on its own; a client could work with many servers and don't depend on any specific server as well as a server could work with any client;
     * `request` initiates only by a client and `response` comes back only from a server
   * Stateless
     * the `server` doesn't remember anything about the user/client who uses the API
     * the `client` holds the state between several requests instead of `server`
     * each individual request contains all the information the server needs to perform the request and return a response
   * Cacheable
     * the server could use cache to make a response if the requested data didn't change between previous last answers. This is can be useful by performance reason
   * Layered system
     * between the client who requests a representation of a resource’s state, and the server who sends the response back, there might be a number of servers in the middle. 
     These servers might provide a security layer, a caching layer, a load-balancing layer, or other functionality. 
     Those layers shouldn't affect the request or the response
   * Code on demand (optional)
     * the client can request code from the server, and then the response from the server will contain some code, usually in the form of a script, when the response is in HTML format. 
     The client then can execute that code.

 * example `RESTful API`
```
 GET /person or /persons
  - get all items (200 OK)
  - is idempotent, can be called without risk of data modification or corruption

 GET /person/:id
  – get one item by id (200 OK, 404 Not Found, 400 Bad Request)
  - is idempotent, can be called without risk of data modification or corruption

 POST /person
  - create one new item (201 Created)
  - is neither safe nor idempotent

 PUT /person/:id
  – replace an existing item by id (200 OK, 204 No Content, 404 Not Found)
  - is neither safe but idempotent operation, it modifies the state of existing item on the server (or creates once a new item)

 PATCH /person/:id
  – partial update an existing item by id (200 OK, 204 No Content, 404 Not Found)
  - is neither safe nor idempotent

 DELETE /person/:id
  - delete one item by id (200 OK, 404 Not Found)
  - is idempotent
```


REST with Spring Tutorial
https://www.baeldung.com/rest-with-spring-series/



## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
...
BUILD SUCCESSFUL in 1m 39s
60 actionable tasks: 60 executed
```

  *  Run server: 
```sh
spring-microservices-course$ ./10-spring-web-rest/build/libs/10-spring-web-rest-all-0.1.jar
                             
_       _____  ______   ______   ______   ______  __    _
| |       | |  | |  | \ | |  | \ | |  | | | |  | \ \ \  | |
| |   _   | |  | |--| < | |__| | | |__| | | |__| |  \_\_| |
|_|__|_| _|_|_ |_|__|_/ |_|  \_\ |_|  |_| |_|  \_\  ____|_|
Dmitriy Shishmakov | Spring Boot

2018-10-16 06:58:22.381  INFO 74207 --- [           main] ru.shishmakov.Main                       : Starting Main on shishmakov.local with PID 74207 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/10-spring-web-rest/build/libs/10-spring-web-rest-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/10-spring-web-rest/build/libs)
2018-10-16 06:58:22.388  INFO 74207 --- [           main] ru.shishmakov.Main                       : No active profile set, falling back to default profiles: default
2018-10-16 06:58:22.463  INFO 74207 --- [           main] ConfigServletWebServerApplicationContext : Refreshing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@74235045: startup date [Tue Oct 16 06:58:22 MSK 2018]; root of context hierarchy
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.springframework.cglib.core.ReflectUtils$1 (jar:file:/Users/dima/programming/git/otus/spring-course/spring-microservices-course/10-spring-web-rest/build/libs/10-spring-web-rest-all-0.1.jar!/BOOT-INF/lib/spring-core-5.0.9.RELEASE.jar!/) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of org.springframework.cglib.core.ReflectUtils$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
2018-10-16 06:58:23.771  INFO 74207 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration' of type [org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration$$EnhancerBySpringCGLIB$$6decdc4b] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-16 06:58:24.345  INFO 74207 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2018-10-16 06:58:24.379  INFO 74207 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2018-10-16 06:58:24.380  INFO 74207 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.34
2018-10-16 06:58:24.393  INFO 74207 --- [ost-startStop-1] o.a.catalina.core.AprLifecycleListener   : The APR based Apache Tomcat Native library which allows optimal performance in production environments was not found on the java.library.path: [/Users/dima/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.]
2018-10-16 06:58:24.512  INFO 74207 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2018-10-16 06:58:24.513  INFO 74207 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2057 ms
...
2018-10-16 06:58:27.468  INFO 74207 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-10-16 06:58:27.469  INFO 74207 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-10-16 06:58:27.488  INFO 74207 --- [           main] .m.m.a.ExceptionHandlerExceptionResolver : Detected @ExceptionHandler methods in restExceptionHandler
2018-10-16 06:58:27.733  INFO 74207 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-10-16 06:58:27.734  INFO 74207 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Bean with name 'dataSource' has been autodetected for JMX exposure
2018-10-16 06:58:27.739  INFO 74207 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
2018-10-16 06:58:27.785  INFO 74207 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2018-10-16 06:58:27.790  INFO 74207 --- [           main] ru.shishmakov.Main                       : Started Main in 6.183 seconds (JVM running for 6.774)
```

 * Client commands to the server:
```sh
curl -vX GET localhost:8080
HTTP/1.1 302
Location: /books
Content-Type: text/plain;charset=UTF-8
Content-Length: 29
Date: Tue, 16 Oct 2018 04:06:07 GMT

RESTful API for Book Library

---
$ curl -X GET localhost:8080/books
[{"id":1,"title":"book 1","isbn":"0-395-08254-1","authors":[1,2,3,4],"genres":[1,2]},{"id":2,"title":"book 2","isbn":"0-395-08254-2","authors":[1,3],"genres":[2]},{"id":3,"title":"book 3","isbn":"0-395-08254-3"},{"id":4,"title":"book 4","isbn":"0-395-08254-4","authors":[4],"genres":[1]}]

---
$ curl -X GET localhost:8080/book/1
{"id":1,"title":"book 1","isbn":"0-395-08254-1","authors":[1,2,3,4],"genres":[1,2]}

---
$ curl -X POST -H "Content-Type: application/json" -d '{"title":"title 1", "isbn":"isbn 1", "authors":[1,2], "genres":[1]}' localhost:8080/book
{"id":5,"title":"title 1","isbn":"isbn 1","authors":[1,2],"genres":[1]}

---
$ curl -X PUT -H "Content-Type: application/json" -d '{"title":"title 5", "isbn":"isbn 5", "authors":[1,4]}' localhost:8080/book/5
{"id":5,"title":"title 5","isbn":"isbn 5","authors":[1,4]}

---
$ curl -vX DELETE localhost:8080/book/5
HTTP/1.1 200
Content-Length: 0
Date: Tue, 16 Oct 2018 04:26:52 GMT
```


## Stop

 * The program is terminated by the response to a user interrupt such as typing `^C` (Ctrl + C) or a system-wide event of a shutdown
```sh
...
2018-10-16 07:00:44.502  INFO 74207 --- [       Thread-2] ConfigServletWebServerApplicationContext : Closing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@74235045: startup date [Tue Oct 16 06:58:22 MSK 2018]; root of context hierarchy
2018-10-16 07:00:44.505  INFO 74207 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
2018-10-16 07:00:44.506  INFO 74207 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans
2018-10-16 07:00:44.552  INFO 74207 --- [       Thread-2] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2018-10-16 07:00:44.554  INFO 74207 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2018-10-16 07:00:44.558  INFO 74207 --- [       Thread-2] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```
