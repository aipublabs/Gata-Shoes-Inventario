FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY inventario/.mvn ./.mvn
COPY inventario/mvnw inventario/mvnw.cmd inventario/pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw -B dependency:go-offline

COPY inventario/src ./src

RUN ./mvnw -B clean package "-Dtest=!InventarioApplicationTests" \
    && cp target/inventario-0.0.1-SNAPSHOT.jar /app/app.jar

FROM eclipse-temurin:17-jre-jammy

RUN groupadd --system appgroup \
    && useradd --system --gid appgroup --create-home appuser

WORKDIR /app
COPY --from=build --chown=appuser:appgroup /app/app.jar ./app.jar

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
