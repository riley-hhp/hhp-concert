# 2단계: 실행 이미지로 OpenJDK 사용
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY ./build/libs/hhp-concert.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]