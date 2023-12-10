#FROM eclipse-temurin:17-jdk-focal
#
#MAINTAINER lfneves
#
#WORKDIR /app
#
#COPY docker-compose.yml /docker-compose.yml
#EXPOSE 8099
#COPY . .
#
#CMD ["./gradlew", "bootRun", "--parallel", "--build-cache"]

#FROM eclipse-temurin:17-jdk-focal
#
#WORKDIR /app
#
#COPY . .
#
#MAINTAINER lfneves
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#COPY docker-compose.yml /docker-compose.yml
#
#EXPOSE 8099
#ENTRYPOINT ["java","-jar","app.jar"]


# First stage: Build the Java artifact
# FROM eclipse-temurin:17-jdk-focal AS builder

# # Copy your Gradle project to the image
# COPY . .

# # Set the working directory
# WORKDIR /app

# # Build the Java artifact
# CMD ["./gradlew", "bootRun", "--parallel", "--build-cache"]

# # Second stage: Create an intermediate image for the JAR file
# FROM builder AS intermediate

# # Copy the JAR file to an intermediate location
# COPY *.jar /app/app.jar

# # Final stage: Use a minimal image for the application
# #FROM eclipse-temurin:17-jdk-focal

# # Copy the JAR file from the intermediate stage
# #COPY --from=intermediate /app/app.jar /app/app.jar

# EXPOSE 8099
# CMD ["java", "-jar", "/app/app.jar"]


# Use an official OpenJDK runtime as a parent image
FROM  eclipse-temurin:17-jdk-focal

RUN mkdir /app

#COPY app.jar /app/app.jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar
COPY docker-compose.yml /docker-compose.yml

WORKDIR /app

EXPOSE 8099

ENTRYPOINT ["java","-jar","app.jar"]



