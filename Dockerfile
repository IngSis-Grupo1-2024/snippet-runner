FROM gradle:8.7.0-jdk17 AS build

ARG ACTOR
ARG TOKEN

ENV GITHUB_ACTOR ${ACTOR}
ENV GITHUB_TOKEN ${TOKEN}

COPY  . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew assemble


FROM eclipse-temurin:17.0.11_9-jre-jammy
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","/app/spring-boot-application.jar"]
