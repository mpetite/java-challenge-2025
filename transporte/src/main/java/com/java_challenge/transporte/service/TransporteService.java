package com.java_challenge.transporte.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_CACHE;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_KEY;

@Service
public class TransporteService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TransporteService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //C
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO createTransporte(Transporte Transporte) {
        try {
            redisTemplate.opsForHash().put(TRANSPORTE_KEY, Transporte.getId().toString(), Transporte.getNombre());
            return new ResponseDTO(HttpStatus.CREATED.value(), "Costo de Transporte creado");
        }
        catch(Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creando Costo de Transporte: " + e.getMessage());
        }
    }

    //R
    @Cacheable(value = TRANSPORTE_CACHE)
    public List<Transporte> getAllPuntosDeVenta() {

        return redisTemplate.opsForHash()
            .entries(TRANSPORTE_KEY)
            .entrySet().stream()
            .map(entry -> new Transporte(
                Long.valueOf(entry.getKey().toString()),
                entry.getValue().toString()
            )).toList();
    }

    //U
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO updateTransporte(Transporte Transporte) {
        try {
            redisTemplate.opsForHash().put(TRANSPORTE_KEY, Transporte.getId().toString(), Transporte.getNombre());
            return new ResponseDTO(HttpStatus.OK.value(), "Costo de Transporte actualizado");
        }
        catch(Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error actualizando Costo de Transporte: " + e.getMessage());
        }
    }
    
    //D
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO deleteTransporte(Long id) {
            redisTemplate.opsForHash().delete(TRANSPORTE_KEY, id.toString());
            return new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Costo de Transporte eliminado");
    }
}