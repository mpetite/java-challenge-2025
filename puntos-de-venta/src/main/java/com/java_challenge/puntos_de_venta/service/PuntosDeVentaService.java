package com.java_challenge.puntos_de_venta.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;

@Service
public class PuntosDeVentaService {

    private final RedisTemplate<String, Object> redisTemplate;

    public PuntosDeVentaService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //C

    //R
    public List<PuntoDeVenta> getAllPuntosDeVenta() {
        return redisTemplate.opsForHash()
            .entries("challenge:puntos-de-venta:1")
            .entrySet().stream()
            .map(entry -> new PuntoDeVenta(
                Long.valueOf(entry.getKey().toString()),
                entry.getValue().toString()
            )).toList();
    }
    //U

    //D

}
