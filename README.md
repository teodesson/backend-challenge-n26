# backend-challenge-n26
Backend Challenge N26

### Challenge
To develop a restful API for statistics. The main use case for the API is to calculate realtime statistic from the last 60 seconds.
There will be two APIs, one of them is called every time a transaction is made (POST /transactions). It is also the sole input of this rest API.
The other one returns the statistic (GET​ ​/statistics) based of the transactions of the last 60 seconds.
**Endpoints have to execute in constant time and memory (O(1))**

### Implementation
I tried to achieve this by Keep It Simple; I'm using predefined numbers of array. 
The precision now is 1 second (configured in AppConfig), increase precision to milisecond will increase the number of arrays to 1000 times more. 

### Running
To run this project : 

```java
mvn spring-boot:run
```

or 

```java
mvn package
java -jar target/backend-challenge-n26-0.0.1-SNAPSHOT.jar
```

### Testing
To run the tests, execute
```java
mvn test