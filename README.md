# E-commerce Project Using Spring Boot, MyBatis & PostGres
- JDBC
- Datasource
- Connection Pool
- Flyway and JDBC Template

Set up/installation:

Clone this project to your own local laptop using
  - git clone `https://github.com/wwon0039/e-commerce-project.git`

Install Postgres via Docker. see
![this link](https://www.docker.com/blog/how-to-use-the-postgres-docker-official-image/) for how to install it

```
Database and mybatis generator

  Generate mybatis files
 $ docker-compose up
 $ cd ECom-app
 $ mvn mybatis-generator:generate -Dmybatis.generator.overwrite=true // generate DAO, mapper and java clasees
 $ docker-compose down

need to put your own paypal id and secret from Paypal developer API credential at https://developer.paypal.com/home/
in ECom-app's application.yml or .env file.  The program won't run without it. And remove it when sharing program.
And create a paypal sandbox account so transaction is not real.

  Start whole landscape
 $ mvn package
 $ docker-compose build
 $ docker-compose up
 
  Import data into elastic search
 $  curl -X POST http://localhost:8080/esProduct/importAll   // should return number of items imported
 

Other nesscary commands
 
 $ docker exec -it e-commerce_mongodb_1 bash     // interact with mongodb    
 # mongosh

 $ docker exec -it e-commerce_postgres_1 psql -U postgres    // interact with postgres
 $ \c springecommerece
 
```
