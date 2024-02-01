# Spring Batch: Tasklet approach

## Create project Spring Batch Step by Step
+ Creaate a project with Spring Boot 3.1.8
+ Add starters
  + Spring Data JPA
  + Lombok
  + Spring Batch
  + Spring Web
  + Spring Boot DevTools
  + MySQL Driver
+ Add starters from maven repository
  ~~~xml
  <!-- https://mvnrepository.com/artifact/com.opencsv/opencsv -->
  <dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.8</version>
  </dependency>

  <!-- https://mvnrepository.com/artifact/commons-compress/commons-compress -->
  <dependency>
    <groupId>commons-compress</groupId>
    <artifactId>commons-compress</artifactId>
    <version>20050911</version>
  </dependency>
  ~~~
+ Change version in pom.xml to version 2.7.18
  ~~~xml
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
  ~~~
+ Create folder package:
  + config
  + controller
  + entities
  + repositories
  + services
  + steps
+ Create the steps
  + DecompressStep
  +