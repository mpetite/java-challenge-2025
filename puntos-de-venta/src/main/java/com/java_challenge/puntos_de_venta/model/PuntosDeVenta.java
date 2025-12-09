package com.java_challenge.puntos_de_venta.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash("PuntosDeVenta")
@Data
public class PuntosDeVenta implements Serializable {

    @Id
    private Long id;
    private String nombre;
}
