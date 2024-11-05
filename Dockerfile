FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw


# Convierte los archivos de Windows a Unix para evitar problemas de formato
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw

RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]