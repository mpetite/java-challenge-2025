package com.java_challenge.puntos_de_venta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.java_challenge.puntos_de_venta"})
@EntityScan(basePackages = {"com.java_challenge.puntos_de_venta"})
public class PuntosDeVentaApp {
    public static void main(String[] args) {
        SpringApplication.run(PuntosDeVentaApp.class, args);
    }
}
