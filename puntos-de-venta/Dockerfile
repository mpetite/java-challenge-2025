FROM ubuntu:latest
LABEL authors="matias.e.petite"

WORKDIR /challenge/puntos_de_venta
COPY src/main/java/com/java_challenge/puntos_de_venta puntos_de_venta

CMD redis-cli -h localhost -p 6379 < users.redis