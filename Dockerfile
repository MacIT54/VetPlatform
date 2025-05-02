FROM gradle:7.6.1-jdk17 as builder

WORKDIR /workspace

# ===== AUTH SERVICE =====
COPY auth-service/gradle ./auth-service/gradle
COPY auth-service/gradlew ./auth-service/
COPY auth-service/build.gradle ./auth-service/
COPY auth-service/settings.gradle ./auth-service/
COPY auth-service/src ./auth-service/src

# ===== PROFILE SERVICE =====
COPY profile-service/gradle ./profile-service/gradle
COPY profile-service/gradlew ./profile-service/
COPY profile-service/build.gradle ./profile-service/
COPY profile-service/settings.gradle ./profile-service/
COPY profile-service/src ./profile-service/src

# ===== APPOINTMENT SERVICE =====
COPY appointment-service/gradle ./appointment-service/gradle
COPY appointment-service/gradlew ./appointment-service/
COPY appointment-service/build.gradle ./appointment-service/
COPY appointment-service/settings.gradle ./appointment-service/
COPY appointment-service/src ./appointment-service/src

# ===== PAYMENT SERVICE =====
COPY payment-service/gradle ./payment-service/gradle
COPY payment-service/gradlew ./payment-service/
COPY payment-service/build.gradle ./payment-service/
COPY payment-service/settings.gradle ./payment-service/
COPY payment-service/src ./payment-service/src

# ===== BUILD =====
RUN cd auth-service && ./gradlew bootJar -x test && mv build/libs/*.jar /workspace/auth.jar
RUN cd profile-service && ./gradlew bootJar -x test && mv build/libs/*.jar /workspace/profile.jar
RUN cd appointment-service && ./gradlew bootJar -x test && mv build/libs/*.jar /workspace/appointment.jar
RUN cd payment-service && ./gradlew bootJar -x test && mv build/libs/*.jar /workspace/payment.jar

# ==========================

FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /workspace/auth.jar /app/auth.jar
COPY --from=builder /workspace/profile.jar /app/profile.jar
COPY --from=builder /workspace/appointment.jar /app/appointment.jar
COPY --from=builder /workspace/payment.jar /app/payment.jar

CMD ["echo", "Use specific command in docker-compose"]
