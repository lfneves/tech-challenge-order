FROM  eclipse-temurin:17-jdk-focal

COPY build/libs/*.jar /app/app.jar
RUN ls -la /app/
WORKDIR /app

EXPOSE 8099
ENTRYPOINT ["java", "-jar", "app.jar"]
