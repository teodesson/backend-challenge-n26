# backend-challenge-n26
Backend Challenge N26

Main problem: endpoints have to execute in constant time and memory (O(1))
I tried to achieve this by Keep It Simple; I'm using predefined numbers of array. The precision now is 1 second (configured in AppConfig), increase precision to milisecond will increase the number of arrays to 1000 times more. 


To run this project : 

mvn spring-boot:run

or 

mvn package
java -jar target/backend-challenge-n26-0.0.1-SNAPSHOT.jar 