FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Xmx512m","-Dserver.port=${PORT}","-jar","/app.jar"]


# docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE='prod'  -e FORUM_DATABASE_URL='jdbc:h2:mem:forum'
# -e FORUM_DATABASE_USER='sa' -e FORUM_DATABASE_PASSWORD='' -e FORUM_JWT_SECRET='123456' forum (nome da imagem)
