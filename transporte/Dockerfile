FROM maven:3.9.9-amazoncorretto-23-alpine AS builder
LABEL authors="matias.e.petite"

WORKDIR /challenge/transporte
COPY . .

RUN mvn clean package -Dmaven.test.skip -f /challenge/transporte/pom.xml && rm -r /challenge/transporte/target
RUN mvn clean package -DskipTests -f /challenge/transporte/pom.xml


FROM amazoncorretto:23-alpine

WORKDIR /challenge/transporte/app
COPY --from=builder /challenge/transporte/target/transporte-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "transporte-0.0.1-SNAPSHOT.jar"]