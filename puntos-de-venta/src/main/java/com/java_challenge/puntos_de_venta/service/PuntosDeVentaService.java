package com.java_challenge.puntos_de_venta.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;
import static com.java_challenge.puntos_de_venta.utils.CommonConstants.PUNTOS_DE_VENTA_CACHE;
import static com.java_challenge.puntos_de_venta.utils.CommonConstants.PUNTOS_DE_VENTA_KEY;

@Service
public class PuntosDeVentaService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public PuntosDeVentaService(RedisTemplate<String, Object> redisTemplate, RabbitTemplate rabbitTemplate) {
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    //C
    @CacheEvict(value = PUNTOS_DE_VENTA_CACHE, allEntries = true)
    public ResponseDTO createPuntoDeVenta(PuntoDeVenta puntoDeVenta) {
        try {
            redisTemplate.opsForHash().put(PUNTOS_DE_VENTA_KEY, puntoDeVenta.getId().toString(), puntoDeVenta.getNombre());
            return new ResponseDTO(HttpStatus.CREATED.value(), "Punto de Venta creado");
        }
        catch(Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creando Punto de Venta: " + e.getMessage());
        }
    }

    //R
    @Cacheable(value = PUNTOS_DE_VENTA_CACHE)
    public List<PuntoDeVenta> getAllPuntosDeVenta() {

        List<PuntoDeVenta> puntosDeVenta = redisTemplate.opsForHash()
            .entries(PUNTOS_DE_VENTA_KEY)
            .entrySet().stream()
            .map(entry -> new PuntoDeVenta(
                Long.valueOf(entry.getKey().toString()),
                entry.getValue().toString()
            )).toList();
        rabbitTemplate.receive("challengeExchange");
        if (rabbitTemplate.receive("challengeExchange").getBody().equals(this))
        rabbitTemplate.convertAndSend("challengeExchange",null , puntosDeVenta);
        return puntosDeVenta;
    }

    //U
    @CacheEvict(value = PUNTOS_DE_VENTA_CACHE, allEntries = true)
    public ResponseDTO updatePuntoDeVenta(PuntoDeVenta puntoDeVenta) {
        try {
            redisTemplate.opsForHash().put(PUNTOS_DE_VENTA_KEY, puntoDeVenta.getId().toString(), puntoDeVenta.getNombre());
            return new ResponseDTO(HttpStatus.OK.value(), "Punto de Venta actualizado");
        }
        catch(Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error actualizando Punto de Venta: " + e.getMessage());
        }
    }
    
    //D
    @CacheEvict(value = PUNTOS_DE_VENTA_CACHE, allEntries = true)
    public ResponseDTO deletePuntoDeVenta(Long id) {
            redisTemplate.opsForHash().delete(PUNTOS_DE_VENTA_KEY, id.toString());
            return new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Punto de Venta eliminado");
    }
}