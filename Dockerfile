FROM openjdk:17
VOLUME /tmp
#EXPOSE 8081
COPY "target/notification-0.0.1-SNAPSHOT.jar" app.jar
ENTRYPOINT ["java","-jar","/app.jar"]