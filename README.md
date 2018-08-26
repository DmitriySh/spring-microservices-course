[![Build Status](https://travis-ci.com/DmitriySh/spring-microservices-course.svg?branch=master)](https://travis-ci.com/DmitriySh/spring-microservices-course)



Spring Framework Developer
=======

Although Spring Framework had been introduced in 2002 it is continue to be the most popular for software development in Java World. The main goal of this course is covering a modern development with Java 8, Spring Core 5.x, Spring Boot 2.x and Spring Cloud.

PS: unfortunately not all other libraries get ready work with Java 9 or 10.

## Requirements:

  * Java SE Development Kit 8  
  * Gradle 4.x (or you could use Gradle wrapper)   
  * Git 1.7.x (or newer) 

## Homework 1: [Quiz for students](https://github.com/DmitriySh/spring-microservices-course/tree/master/01-spring-xml)
Configuration. Use class [ClassPathXmlApplicationContext](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/support/ClassPathXmlApplicationContext.html) 
and xml file for configuration

## Homework 2: [Quiz for students](https://github.com/DmitriySh/spring-microservices-course/tree/master/02-spring-annotation)
Configuration. Use class [AnnotationConfigApplicationContext](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/AnnotationConfigApplicationContext.html) 
and annotations [@Configuration](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/Configuration.html), 
[@ComponentScan](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/context/annotation/ComponentScan.html), 
[@Service](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/stereotype/Service.html)
for configuration

## Homework 3: [Quiz for students](https://github.com/DmitriySh/spring-microservices-course/tree/master/03-spring-boot-start)
Spring Boot. Use class [SpringApplication](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-spring-application.html) 
and annotation [@SpringBootApplication](https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html) for configuration

## Homework 4: [Quiz for students](https://github.com/DmitriySh/spring-microservices-course/tree/master/04-spring-shell)
Spring Shell. Use annotation [@ShellComponent](https://docs.spring.io/spring-shell/docs/2.0.x/api/org/springframework/shell/standard/ShellComponent.html) 
like a new `@Component` stereotype to enable terminal client

## Homework 5: [Books library](https://github.com/DmitriySh/spring-microservices-course/tree/master/05-spring-jdbc)
Spring JDBC. Use DAO, [JdbcTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html) 
and [TransactionTemplate](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/support/TransactionTemplate.html) 
to simplify using of JDBC and programmatic transactions

## Homework 6: [Books library](https://github.com/DmitriySh/spring-microservices-course/tree/master/06-spring-orm-jpa)
Spring ORM JPA. Use [EntityManagerFactory](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManagerFactory.html) 
and [PlatformTransactionManager](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html) 
that covers persistence technologies

## Homework 7: [Books library](https://github.com/DmitriySh/spring-microservices-course/tree/feature/07-spring-data-jpa/07-spring-data-jpa)
Spring Data JPA. Use [Repository](https://docs.spring.io/spring-data/data-commons/docs/2.1.x/api/org/springframework/data/repository/Repository.html) interface
and [@Repository](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/stereotype/Repository.html) annotation to implement patterns DAO
