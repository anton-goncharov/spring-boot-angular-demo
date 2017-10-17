Calorie Counter Application
===
Developed as a part of the Toptal screening process.

#### Technologies:
* Java 8
* Spring Boot (Spring REST + Spring Data JPA + Spring Security)
* AngularJS

Designed to work with MySQL 5+ database.

#### Installation guide:
1. Create database schema and privileged user, populate schema with objects and data from '/scripts/schema.sql' and '/scripts/init-data.sql'
2. Edit __resources/application.properties__ file. Check your database url and credentials. Pay attention to 'uploads.root' property.
3. To run in an embedded servlet container:
    1. Execute 'mvn spring-boot:run' in command line
4. To run in a stand-alone Tomcat:
    1. Execute 'mvn web:web' in command line
    2. Copy war archive from 'target' subfolder to Tomcat 'webapps' directory
5. Browse to [localhost:8080](localhost:8080)

Default admin username: __admin@mail.com__, password: __123__
