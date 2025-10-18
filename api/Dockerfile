FROM gradle:jdk25-alpine AS build
WORKDIR /application
COPY build.gradle* settings.gradle* ./
COPY src/ src/
RUN --mount=type=cache,target=/home/gradle/.gradle gradle clean build -x test

FROM eclipse-temurin:25-alpine AS layers
WORKDIR /application
COPY --from=build /application/build/libs/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM eclipse-temurin:25-alpine AS production
RUN adduser -S spring-user
USER spring-user

WORKDIR /application

COPY --from=layers /application/extracted/dependencies/ ./
COPY --from=layers /application/extracted/spring-boot-loader/ ./
COPY --from=layers /application/extracted/snapshot-dependencies/ ./
COPY --from=layers /application/extracted/application/ ./

RUN java -XX:ArchiveClassesAtExit=app.jsa -Dspring.context.exit=onRefresh -jar app.jar & exit 0

ENTRYPOINT exec java -jar app.jar