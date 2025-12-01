FROM maven:3.9.9-amazoncorretto-23-alpine as builder
LABEL authors="matias.e.petite"

WORKDIR /challenge/puntos_de_venta
COPY . .

RUN mvn clean package -Dmaven.test.skip -f /challenge/puntos_de_venta/pom.xml && rm -r /challenge/puntos_de_venta/target
RUN mvn clean package -DskipTests -f /challenge/puntos_de_venta/pom.xml


FROM amazoncorretto:23-alpine

WORKDIR /challenge/puntos_de_venta/app
COPY --from=builder /challenge/puntos_de_venta/target/puntos_de_venta-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "puntos_de_venta-0.0.1-SNAPSHOT.jar"]