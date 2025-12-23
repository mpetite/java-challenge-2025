package com.java_challenge.transporte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.java_challenge.transporte"})
@EntityScan(basePackages = {"com.java_challenge.transporte"})
@EnableCaching
public class PuntosDeVentaApp {
    public static void main(String[] args) {
        SpringApplication.run(PuntosDeVentaApp.class, args);
    }
}
