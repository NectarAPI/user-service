FROM amazoncorretto:17-alpine-jdk
MAINTAINER dev@nectar.software
WORKDIR /etc/user-service
ARG JAR_FILE=build/libs/user-service-3.0.0-alpha.jar
COPY ${JAR_FILE} user-service.jar
ENTRYPOINT ["java","-jar","user-service.jar"]
