FROM gradle:7.5.1-jdk17-alpine AS builder

COPY . /usr/tmp
WORKDIR /usr/tmp
RUN gradle clean build -x test --no-daemon


FROM eclipse-temurin:17-alpine
EXPOSE 9090
COPY --from=builder /usr/tmp/build/libs/dental-clinic-service-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "dental-clinic-service-0.0.1-SNAPSHOT.jar"]