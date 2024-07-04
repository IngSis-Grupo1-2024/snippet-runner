FROM gradle:8.7.0-jdk17 AS build

ARG ACTOR
ARG TOKEN

ENV GITHUB_ACTOR ${ACTOR}
ENV GITHUB_TOKEN ${TOKEN}

COPY  . /app
WORKDIR /app
RUN ./gradlew bootJar


WORKDIR /home/gradle/src

FROM amazoncorretto:17-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /app/build/libs/snippet-runner.jar /app/snippet-runner.jar
COPY newrelic/newrelic.jar /app/newrelic.jar
COPY newrelic/newrelic.yml /app/newrelic.yml
RUN touch /app/lint.json
RUN touch /app/format.json
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","-javaagent:/app/newrelic.jar","/app/snippet-runner.jar"]