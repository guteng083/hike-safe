FROM amazoncorretto:17-alpine
VOLUME /tmp
COPY target/*.jar app.jar
RUN mkdir /logs
ENTRYPOINT ["java","-jar","/app.jar"]