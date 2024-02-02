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
  + ReaderStep
  + ProcessorStep
  + WriterStep
+ Create the Batch configuration
  + Es una clase, en nuestro caso con nombre `BatchConfig`
  + Anotado con `@Configuration` y `@EnableBatchProcessing`
  + Tienen 3 seccciones:
    + Definición de los items jobs
      Se tratan de métodos con `@Bean` y `@JobScope` que devuelve un objeto del tipo de la clases Steps definidas para el proceso batch.
      ~~~java
      @Bean
      @JobScope
      public DecompressStep decompressStep() {
        return new DecompressStep(this.resourceLoader);
      }
      ~~~
    + Definición de los tasklet steps
      Se tratan de métodos anotados con `@Bean` que devuelven un Step construido con `stepBuilderFactory`.
      ~~~java
      @Bean
      public Step decompressStepBuild(){
          return stepBuilderFactory.get("DecompressStep")
                  .tasklet(decompressStep())
                  .build();
      }
      ~~~
    + Definición del Job
      Este método utiliza el `jobBuilderFactory` para crear el Job definiendo el Step inicial con el método `start`, y los pasos siguientes con el método `next`, en el orden en el que se desea procesar.
      ~~~java
      @Bean
      public Job batchTaskletJob() {
          return jobBuilderFactory.get("batchTaskletJob")
                  .start(decompressStepBuild())
                  .next(readerStepBuild())
                  .next(processorStepBuild())
                  .next(writerStepBuild())
                  .build();
      }
      ~~~
+ Checking the application.yml
  ~~~yml
  spring:
    datasource:
      url: jdbc:mysql://<host>:<port>/<database>
      username: <user>
      password: <password>
      driver-class-name: com.mysql.cj.jdbc.Driver
    batch:
      job:
        enabled: false
      jdbc:
        initialize-schema: always
    jpa:
      hibernate:
        ddl-auto: update
    servlet:
      multipart:
        enabled: true
        max-file-size: 10MB
        max-request-size: 10MB
  ~~~
  + `spring.batch.job.enabled: false`: Esta propiedad desactiva la ejecución automática de trabajos de Spring Batch al inicio de la aplicación. Si se establece en true, todos los trabajos de Spring Batch se ejecutarán automáticamente al inicio de la aplicación.
  + `spring.batch.jdbc.initialize-schema: always`: Esta propiedad controla la inicialización del esquema de base de datos para Spring Batch.
    + `always`: Esto significa que el esquema de la base de datos se inicializará siempre que se inicie la aplicación. Las tablas necesarias para Spring Batch se crearán automáticamente si no existen.
    + `embedded`: Esto significa que el esquema de la base de datos se inicializará solo si la base de datos es de tipo embebida (como H2, HSQL o Derby). Las tablas necesarias para Spring Batch se crearán automáticamente si no existen.
    + `never`: Esto significa que el esquema de la base de datos no se inicializará automáticamente. Tendrás que crear las tablas necesarias para Spring Batch manualmente.
  + `spring.jpa.hibernate.ddl-auto: update`: Esta propiedad controla la generación automática del esquema de la base de datos.
    + `none:` No se realiza ninguna acción en el esquema de la base de datos.
    + `validate:` Hibernate valida que las tablas y columnas que necesita existen, y lanzará una excepción si no existen.
    + `update:` Hibernate actualizará el esquema de la base de datos si es necesario, creando nuevas tablas y columnas si no existen, pero no borrará las existentes.
    + `create:` Hibernate creará las tablas y columnas necesarias al inicio de la sesión, pero no borrará las existentes al final.
    + `create-drop:` Hibernate creará las tablas y columnas necesarias al inicio de la sesión, y las borrará al final.
    + `delimited:` Similar a create-drop, pero las sentencias DDL están delimitadas por puntos y comas.  
    
    Por favor, ten en cuenta que `create`, `create-drop` y `delimited` son principalmente para pruebas y desarrollo, y no se recomiendan para producción.
  + `spring.servlet.multipart.enabled: true`: Esta propiedad habilita el soporte para la carga de archivos multipartes en la aplicación.
  + `spring.servlet.multipart.max-file-size: 10MB`: Esta propiedad establece el tamaño máximo de archivo que se puede cargar. En este caso, se establece en 10MB.
  + `spring.servlet.multipart.max-request-size: 10MB`: Esta propiedad establece el tamaño máximo de la solicitud que se puede hacer. Esto incluye el tamaño de todos los archivos que se están cargando en una sola solicitud. En este caso, también se establece en 10MB.

+ Checking the application
  + Testing HealthCheck method  
    ![Texto alternativo][PostmanSpringBatchHealth]
  + Testing uploadFile method
    ![image][PostmanSpringBatchUpload]
  + Checking the database
    ![image][PostmanSpringBatchDatabase]

[PostmanSpringBatchHealth]: ./images/postman.spring-batch-health.png
[PostmanSpringBatchUpload]: ./images/postman.spring-batch-upload.png
[PostmanSpringBatchDatabase]: ./images/postman.spring-batch-database.png
