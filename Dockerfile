FROM gradle:7.5.1-jdk17-alpine AS builder

COPY src /usr/tmp/src
COPY build.gradle.kts /usr/tmp
COPY settings.gradle.kts /usr/tmp
WORKDIR /usr/tmp
RUN gradle bootJar --no-daemon


FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=builder /usr/tmp/build/libs/*.jar /usr/app/main.jar
RUN chown -R javauser:javauser /usr/app
USER javauser
WORKDIR /usr/app

ENTRYPOINT ["java", "-Dserver.port=${SERVICE_PORT}", "-Dspring.profiles.active=${ACTIVE_PROILE}", "-jar", "main.jar"]