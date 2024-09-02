FROM openjdk:17
WORKDIR /test
COPY target/*.jar task-organizer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "task-organizer.jar"]