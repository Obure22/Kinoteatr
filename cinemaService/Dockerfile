FROM eclipse-temurin:25-jdk-jammy as build

WORKDIR /build

COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml

COPY cinemaService/pom.xml cinemaService/pom.xml
COPY authService/pom.xml authService/pom.xml
COPY searchService/pom.xml searchService/pom.xml
COPY analyzeService/pom.xml analyzeService/pom.xml

RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw -pl cinemaService -am dependency:go-offline -DskipTests

COPY cinemaService/src cinemaService/src

RUN --mount=type=cache,target=/root/.m2 ./mvnw -pl cinemaService -am package -DskipTests

FROM eclipse-temurin:25-jre-jammy AS final

WORKDIR /app

COPY --from=build /build/cinemaService/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]