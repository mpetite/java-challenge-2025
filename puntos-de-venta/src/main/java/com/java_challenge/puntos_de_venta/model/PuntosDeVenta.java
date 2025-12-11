package com.java_challenge.puntos_de_venta.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash("puntos-de-venta:1000")
@Data
public class PuntosDeVenta implements Serializable {

    @Id
    private Long id;
    private String nombre;
}
