package com.java_challenge.puntos_de_venta.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;

@Service
public class PuntosDeVentaService {

    private final RedisTemplate<String, Object> redisTemplate;

    public PuntosDeVentaService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //C
    public ResponseDTO createPuntoDeVenta(PuntoDeVenta puntoDeVenta) {
        try {
            redisTemplate.opsForHash().put("challenge:puntos-de-venta:1", puntoDeVenta.getId().toString(), puntoDeVenta.getNombre());
            return new ResponseDTO(201, "Punto de Venta creado");
        }
        catch(Exception e) {
            return new ResponseDTO(500, "Error creando Punto de Venta: " + e.getMessage());
        }
    }

    //R
    @Cacheable(value = "puntosDeVentaCache")
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
