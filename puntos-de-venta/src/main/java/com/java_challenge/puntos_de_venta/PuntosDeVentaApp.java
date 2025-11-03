package com.java_challenge.puntos_de_venta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.java_challenge")
public class PuntosDeVentaApp {
    public static void main(String[] args) {
        SpringApplication.run(PuntosDeVentaApp.class, args);
    }
}
