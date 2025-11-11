package com.java_challenge.puntos_de_venta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Data
@RedisHash("PuntosDeVenta")
public class PuntosDeVenta {

    @Id
    private Long id;
    private String nombre;
}
