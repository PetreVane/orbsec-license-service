FROM openjdk:11-slim
LABEL maintainer="Petre Vane <petre.vane@gmail.com>"
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

