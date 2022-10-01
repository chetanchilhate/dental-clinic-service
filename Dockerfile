FROM gradle:7.5.1-jdk17-alpine AS builder

COPY . /usr/tmp
WORKDIR /usr/tmp
RUN gradle build -x test --no-daemon


FROM eclipse-temurin:17-alpine
EXPOSE 8080
COPY --from=builder /usr/tmp/build/libs/dental-clinic-service-0.0.1.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-Dserver.port=${SERVICE_PORT}", "-Dspring.profiles.active=${ACTIVE_PROILE}", "-jar", "dental-clinic-service-0.0.1.jar"]