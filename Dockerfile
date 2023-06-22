FROM openjdk:17

ADD ./build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
#
## Base image
#FROM adoptopenjdk:17-jdk-hotspot
#
## Set the working directory
#WORKDIR /app
#
## Copy the Gradle build files
#COPY build.gradle .
#COPY settings.gradle .
#COPY gradlew .
#
## Copy the Gradle wrapper files
#COPY gradle gradle
#
## Download and cache Gradle dependencies
#RUN ./gradlew --version
#
## Copy the project source code
#COPY . .
#
## Build the application
#RUN ./gradlew clean build
#
## Set the entry point for the application
#ENTRYPOINT ["java", "-war", "build/libs/your-application-name.war"]
