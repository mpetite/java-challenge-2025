package com.java_challenge.puntos_de_venta.service;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;
import com.java_challenge.puntos_de_venta.repositories.PuntosDeVentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntosDeVentaService {

    private final PuntosDeVentaRepository puntosDeVentaRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public PuntosDeVentaService(PuntosDeVentaRepository puntosDeVentaRepository, RedisTemplate<String, Object> redisTemplate;) {
        this.puntosDeVentaRepository = puntosDeVentaRepository;
        this.redisTemplate = redisTemplate;
    }

    //C

    //R
    public List<PuntosDeVenta> getAllPuntosDeVenta() {
        return puntosDeVentaRepository.findAll();
    }

    public Map<Object, Object> getAllPuntosDeVenta2() {
        return redisTemplate.opsForHash()
            .entries("puntos-de-venta:1000");
    }
    //U

    //D

}
