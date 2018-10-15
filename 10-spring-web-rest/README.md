Books library
=======
Use `Spring Boot 2.x` and `Spring Web MVC 5.x`.<br/>
Most classical applications work on the Internet and use a web browser to communicate with the server by HTTP protocol to send a request and get a response back. HTML pages are a common response used for communication between a server and a client.
`Spring Boot` is well suited for web application development: create a self-contained HTTP server by using embedded Tomcat, Jetty or Undertow.
Most web applications use the `spring-boot-starter-web` module to get up and running quickly.


## Description
 * The program shows information about books in the Library
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
   * servlets are written on Java and need to handle requests and dynamically generate the web pages on the server side
   * support authentificaion, authorization and http sessions

 * The `Spring Framework` is an application framework and IoC container for the Java platform.
 * `Spring Boot` is not a Framework, it is Spring's [convention over configuration](https://en.wikipedia.org/wiki/Convention_over_configuration) solution for creating stand-alone, production-grade Spring-based applications.
 * Convention over configuration it is a software design paradigm used by software frameworks that attempt to decrease complexity without losing flexibility.

 - REST (REpresentational State Transfer) is an architectural style that defines a set of constraints to be used for creating distributed application in the network. This is how web services interact
 - REST is not a framework, a protocol or communication format it is an architectural style, or design pattern for APIs
 - REST was defined by Roy Fielding, a computer scientist. He presented the REST principles in his PhD dissertation in 2000

 - The system supporting `REST` is a `RESTful`, each unit of information is URL with already defined standard declarations based on HTTP
 - HTTP commands are used for CRUD operations: GET (retrieve), PUT (update/modify), POST (create), DELETE (remove)
 - It means when a RESTful API is called, the server will use HTTP command and transfer to the client a representation state of the requested resource
 - The representation of the state can be in a JSON format and probably for most APIs this is indeed the case. It can also be in XML or HTML format
 

RESTful API should follow 6 constraints:
 - Uniform interface
   Interactions client-server should be defined by predefined rules
   1) each resource must be uniquely identified by a stable identifier. A `stable` identifier means that it doesn't change across interactions, and it doesn't change even when the state of the resource changes
   2) the response the server returns include enough information to modify or delete the resource
   3) each request from the client should contain all the information the server needs to process its request, and each response from the server should contain all the information the client needs in order to understand the response
   4) hypermedia. It is a data sent from the server to the client that contains information about what the client can do next–in other words, what further requests it can make. In REST, servers should be sending only hypermedia to clients. HTML links are a type of hypermedia.
 - Client–server
   - communication beetween 2 separated items: `cleint` and `server`; 
   - the client and the server act independently, each on its own; a client could work with many servers and don't depend on any specific server as well as a server could work with any client;
   - `request` initiates only by a client and `response` comes back only from a server
 - Stateless
   - the `server` doesn't remember anything about the user/client who uses the API
   - the `client` holds the state between several requests instead of `server`
   - each individual request contains all the information the server needs to perform the request and return a response
 - Cacheable
   - the server could use cache to make a response if the requested data didn't change between previous last answers. This is can be useful by performance reason
 - Layered system
   - between the client who requests a representation of a resource’s state, and the server who sends the response back, there might be a number of servers in the middle. These servers might provide a security layer, a caching layer, a load-balancing layer, or other functionality. Those layers shouldn't affect the request or the response
 - Code on demand (optional)
   - the client can request code from the server, and then the response from the server will contain some code, usually in the form of a script, when the response is in HTML format. The client then can execute that code.

 - example RESTful API
```
 GET /person
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
  - is neither safe but idempotent operation, it modifies (or creates) state on the server

 PATCH /person/:id
  – partial update an existing item by id (200 OK, 204 No Content, 404 Not Found)
  - is neither safe nor idempotent

 DELETE /person/:id
  - delete one item by id (200 OK, 404 Not Found)
  - is idempotent
```



## Run
 *  Build project
```sh
spring-microservices-course$ ./gradlew clean build
                             
> Configure project :
Version 0.1
...
BUILD SUCCESSFUL in 1m 10s
54 actionable tasks: 54 executed
```

  *  Run server: 
```sh
spring-microservices-course$ ./09-spring-mvc/build/libs/09-spring-mvc-all-0.1.jar

 _       _____  ______   ______   ______   ______  __    _
| |       | |  | |  | \ | |  | \ | |  | | | |  | \ \ \  | |
| |   _   | |  | |--| < | |__| | | |__| | | |__| |  \_\_| |
|_|__|_| _|_|_ |_|__|_/ |_|  \_\ |_|  |_| |_|  \_\  ____|_|
Dmitriy Shishmakov | Spring Boot

2018-09-27 08:28:22.035  INFO 21298 --- [           main] ru.shishmakov.Main                       : Starting Main on shishmakov.local with PID 21298 (/Users/dima/programming/git/otus/spring-course/spring-microservices-course/09-spring-mvc/build/libs/09-spring-mvc-all-0.1.jar started by dima in /Users/dima/programming/git/otus/spring-course/spring-microservices-course/09-spring-mvc/build/libs)
2018-09-27 08:28:22.041  INFO 21298 --- [           main] ru.shishmakov.Main                       : No active profile set, falling back to default profiles: default
2018-09-27 08:28:22.109  INFO 21298 --- [           main] ConfigServletWebServerApplicationContext : Refreshing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@b065c63: startup date [Thu Sep 27 08:28:22 MSK 2018]; root of context hierarchy
2018-09-27 08:28:23.269  INFO 21298 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration' of type [org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration$$EnhancerBySpringCGLIB$$2d6d6e7e] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-09-27 08:28:23.674  INFO 21298 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2018-09-27 08:28:23.722  INFO 21298 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2018-09-27 08:28:23.723  INFO 21298 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.32
2018-09-27 08:28:23.736  INFO 21298 --- [ost-startStop-1] o.a.catalina.core.AprLifecycleListener   : The APR based Apache Tomcat Native library which allows optimal performance in production environments was not found on the java.library.path: [/Users/dima/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.]
2018-09-27 08:28:23.813  INFO 21298 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2018-09-27 08:28:23.813  INFO 21298 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1707 ms
...
2018-09-27 08:28:25.996  INFO 21298 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-09-27 08:28:25.996  INFO 21298 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2018-09-27 08:28:26.359  INFO 21298 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-09-27 08:28:26.360  INFO 21298 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Bean with name 'dataSource' has been autodetected for JMX exposure
2018-09-27 08:28:26.365  INFO 21298 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
2018-09-27 08:28:26.413  INFO 21298 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2018-09-27 08:28:26.416  INFO 21298 --- [           main] ru.shishmakov.Main                       : Started Main in 4.787 seconds (JVM running for 5.31)
```


## Stop

 * The program is terminated at the end of quiz, by command `exit` or response to a user interrupt, such as typing `^C` (Ctrl + C), or a system-wide event of a shutdown.
```sh
...
2018-09-27 08:30:30.850  INFO 21332 --- [       Thread-3] ConfigServletWebServerApplicationContext : Closing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@b065c63: startup date [Thu Sep 27 08:30:22 MSK 2018]; root of context hierarchy
2018-09-27 08:30:30.852  INFO 21332 --- [       Thread-3] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
2018-09-27 08:30:30.853  INFO 21332 --- [       Thread-3] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans
2018-09-27 08:30:30.856  INFO 21332 --- [       Thread-3] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2018-09-27 08:30:30.857  INFO 21332 --- [       Thread-3] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2018-09-27 08:30:30.859  INFO 21332 --- [       Thread-3] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```
