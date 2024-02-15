# Getting Started

## Nice to know
You don't have to install PostgreSQL unless you want to run the application locally. 
When running and building the project with maven commands, we just use an embedded PostgreSQL database.

### (Optional) Windows - Installing PostgreSQL database for running application local machine
- Go to https://www.enterprisedb.com/downloads/postgres-postgresql-downloads and download PostgreSQL for windows
- Run the installer. Choose whatever file location that best suits you. Username and password should be postgres / postgres
- Full guide can be found here: https://www.postgresqltutorial.com/postgresql-getting-started/install-postgresql/
Make sure you add the PostgreSQL bin directory to the PATH environment variable

#### Windows - Connect to PostgreSQL
In the Windows Command Prompt, run the command where "userName" is the username you chose in the previous step:
    
    psql -U userName

Enter your password when prompted.

#### Windows - Change password and create database

    ALTER USER postgres with password 'postgres';
    CREATE DATABASE bdd_workshop1;
    \q   <-- quit

### (Optional) Ubuntu / Linux - Installing PostgreSQL database for running the application on local machine
Run the following command from a terminal window:
    
    sudo apt update sudo apt install postgresql postgresql-contrib
 
#### Ubuntu / Linux - Connect to PostgreSQL
Open the command line and type in the following
    sudo -i -u postgres
    psql

#### Ubuntu / Linux - Change password and create database

    alter user postgres with password 'postgres';
    create database bdd_workshop1;
    \q   <-- quit

### Access to API documentation when running on local machine
    http://localhost:8080/docs/swagger-ui/index.html

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#web)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#actuator)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.flyway)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### License

This workshop is [Apache 2.0 licensed](./LICENSE).
