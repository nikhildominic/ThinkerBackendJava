FROM openjdk:11
EXPOSE 8080
ADD target/thinkerbackend.jar thinkerbackend.jar
ENTRYPOINT ["java", "-jar", "/thinkerbackend.jar"]