Books library
=======
Use `Spring Boot 2.x` and `Spring Web MVC 5.x`.<br/>
Most modern applications work in the Internet and web browsers use HTTP protocol to communicate with them by requests and responses.
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

 * `Spring MVC` (model-view-controller) is designed around the [Front Controller](https://en.wikipedia.org/wiki/Front_controller) pattern:
   * The `Front Controller` provides a centralized entry point for that controls and manages web request handling
   * In `Spring MVC` [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) is a front controller who handles all the user request and process the request as per there mapping
   * [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) is completely integrated with the Spring IoC container

 * Flow and lifecycle in Spring MVC:
   * request will be received by [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) as the first step
   * [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) gets the help of HandlerMapping and request gets transferred to the `@Controller` associated class
   * then `@Controller` will process the request by executing appropriate methods and returns `ModeAndView` object (contains both Model data and View name) back to the [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html)
   * [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) send the model object to the `ViewResolver` to resolve and retrieve the actual view page
   * [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) will pass the Model object to the View page to display the result and create the response
   * finally [DispatcherServlet](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) sends the response back to the browser

![](https://raw.githubusercontent.com/javatutorials2016/jp/master/img/spring-flow.png)

 * In a standalone application the main HTTP port defaults to `8080`, but can be set with `server.port`
 * To switch off the HTTP endpoints completely but still create a `WebApplicationContext` use `server.port=-1`
 * To scan for a free port use `server.port=0`
 * To get the value of random port:
   * create bean ApplicationListener<EmbeddedServletContainerInitializedEvent> and pull the container out of the event 
   when it is published
   * use `@LocalServerPort` or `@Value("${local.server.port}")` on a field if you will use 
   `@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)`

 * By default, the context path is "/" but you could change it:
   * via property `server.servlet.contextPath=/newpath`
   * to make a custom `@Component` and get access to the server factory through the implementation of the interface `WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>` and set `container.setContextPath("/newpath")`

 * Embedded `Apache Tomcat` is a default servlet container in a `Spring Boot 2.x` and you don't need to specify concrete module `spring-boot-starter-tomcat` cause it is a part of main `spring-boot-starter-web`
 * Embedded `Apache Tomcat` could be replaced to embeded `Jetty`, `Undertow` or `Netty` (not support Servlet stack)

 * [Thymeleaf](https://www.thymeleaf.org) is a modern server-side Java natural template engine for both web (servlet-based) and standalone environments:
   * template on Thymeleaf could work and test at the time of development without special runtime context because web browsers ignore additional tag attributes
   * it is work with different template modes:  XML/XHTML/TEXT/JS/CSS/RAW
   * it is open-source software, licensed under the Apache License 2.0 but originally created by Software Engineer Daniel Fernández
   * in web applications Thymeleaf aims to be a complete substitute for JSP (old technology that helps create dynamically generated web pages based on HTML/XML instead of using servlets directly)
   * it has integration packages for Spring Framework 3.x, 4.x and 5.x
   * `thymeleaf-testing` it is a testing framework, allows users to test the view layer of their applications

 * [Thymeleaf](https://www.thymeleaf.org) features:
   * injects own attributes in base elements of HTML5 document or other documents
   * add a namespace for additional attributes of Thymeleaf `<html xmlns:th="http://www.thymeleaf.org">` or use prefix "data-" for each Thymeleaf tags
   ```
  	<body>
    	<p th:text="#{home.welcome}">Welcome to our grocery store!</p>
  	</body>
  	...
  	<body>
    	<p data-th-text="#{home.welcome}">Welcome to our grocery store!</p>
  	</body>  	   
   ```
   * it comes with standard dialects (Standard and SpringStandard) that define a set of features which should be helpful for the most scenarios
   * thymeleaf attributes allow their values to be set as or containing standard expression syntax:
     * `${...}` : variable expressions
     * `*{...}` : selection expressions
     * `#{...}` : message (i18n) expressions
     * `@{...}` : link (URL) expressions
     * `~{...}` : fragment expressions

   * variable expressions are `OGNL expressions` or `SpEL` if you’re integrating Thymeleaf with Spring `<span th:text="${book.author.name}">` is equal `((Book)context.getVariable("book")).getAuthor().getName()`
   * selection expressions will be executed on a previously selected object instead of the whole context variables map
   ```
     <div th:object="${book}">
       ...
       <span th:text="*{title}">...</span>
       ...
     </div>
   ```


Spring Boot, Developing Web Applications
https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-developing-web-applications.html

Spring Web MVC
https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/web.html#mvc

Thymeleaf Tutorial
https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#introducing-thymeleaf


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
