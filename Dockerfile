FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY pom.xml .
RUN mkdir -p src && echo " " > src/Placeholder.java
RUN ./mvnw dependency:go-offline -B

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE ${API_PORT:-8080}

# Comando de execução
ENTRYPOINT ["java", "-jar", "target/todo-api-0.0.1-SNAPSHOT.jar"]