FROM maven:3.8.4-openjdk-8 AS build
COPY . .
RUN mvn  clean package -DskipTests

FROM openjdk:8-jre-slim
COPY --from=build /target/nanda-betest-0.0.1-SNAPSHOT.jar nanda-betest.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","nanda-betest.jar"]