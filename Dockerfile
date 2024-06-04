FROM gradle:8.7.0-jdk17 AS build

ARG ACTOR
ARG TOKEN

ENV GITHUB_ACTOR ${ACTOR}
ENV GITHUB_TOKEN ${{secrets.TOKEN}}
COPY  . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble
FROM openjdk:23-ea-17-jdk-slim
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","/app/spring-boot-application.jar"]
