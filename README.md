# Phone book

Application support MySQL and CSV repositories

## Technology stack

 - JDK 1.8
 - Maven
 - Spring Boot
 - Thymeleaf
 - Tomcat 8
 - JQuery
 - Bootstrap

## Build and run application

./mvnw clean package && java -Dlardi.conf=repository/config/sample-mysql.properties -Dspring.profiles.active=mysql -jar target/phone-book-0.0.1-SNAPSHOT.jar
Go to [http://localhost:8080/](http://localhost:8080/) to login page

## Sample properties

#### MySQL ([repository/config/sample-mysql.properties](repository/config/sample-mysql.properties))

```properties
lardi.datasource.url=jdbc:mysql://localhost/phonebook
lardi.datasource.username=root
lardi.datasource.password=
lardi.datasource.driver-class-name=com.mysql.jdbc.Driver
``` 

#### CSV ([repository/config/sample-csv.properties](repository/config/sample-csv.properties))

```properties
lardi.datasource.path=repository/csv
``` 

## Tests

#### Unit tests

./mvnw clean test

#### DB and REST tests

###### MySQL

./mvnw clean -P integration-test,mysql test

###### CSV

./mvnw clean -P integration-test,csv test


#### Run with maven

###### MySQL

./mvnw spring-boot:run -Drun.jvmArguments="-Dlardi.conf=repository/config/sample-mysql.properties -Dspring.profiles.active=mysql"

###### CSV

./mvnw spring-boot:run -Drun.jvmArguments="-Dlardi.conf=repository/config/sample-csv.properties -Dspring.profiles.active=csv"

## MySQL TABLES

```sql
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` CHAR(36) NOT NULL,
  `login` VARCHAR(255) NOT NULL ,
  `password` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS contact (
  `contact_id` CHAR(36) NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NOT NULL,
  `middle_name` VARCHAR(255) NOT NULL,
  `mobile_phone_number` CHAR(17) NOT NULL,
  `home_phone_number` CHAR(17),
  `address` VARCHAR(255),
  `email` VARCHAR(255),
  PRIMARY KEY(`contact_id`),
  CONSTRAINT `fk_contact_user` FOREIGN KEY (`user_id`)
  REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
``` 
