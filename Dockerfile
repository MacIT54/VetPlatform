FROM gradle:7.6.1-jdk17 as builder

WORKDIR /workspace

COPY auth-service/gradle ./auth-service/gradle
COPY auth-service/gradlew ./auth-service/
COPY auth-service/build.gradle ./auth-service/
COPY auth-service/settings.gradle ./auth-service/
COPY auth-service/src ./auth-service/src

COPY profile-service/gradle ./profile-service/gradle
COPY profile-service/gradlew ./profile-service/
COPY profile-service/build.gradle ./profile-service/
COPY profile-service/settings.gradle ./profile-service/
COPY profile-service/src ./profile-service/src

RUN cd auth-service && ./gradlew bootJar -x test && mv build/libs/*.jar /workspace/auth.jar
RUN cd profile-service && ./gradlew bootJar -x test && mv build/libs/*.jar /workspace/profile.jar

FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /workspace/auth.jar /app/auth.jar
COPY --from=builder /workspace/profile.jar /app/profile.jar

CMD ["echo", "Use specific command in docker-compose"]