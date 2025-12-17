package com.java_challenge.puntos_de_venta;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;
import com.java_challenge.puntos_de_venta.service.PuntosDeVentaService;

@ExtendWith(MockitoExtension.class)
public class PuntosDeVentaServiceTests {

    private static final String REDIS_KEY = "challenge:puntos-de-venta:1";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private PuntosDeVentaService service;

    private Map<Object, Object> redisData;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        redisData = new HashMap<>();
        redisData.put("1", "CABA");
        redisData.put("2", "GBA_1");
    }

    @Test
    void givenExistingData_whenGetAllPuntosDeVenta_thenReturnsList() {

        //given
        when(hashOperations.entries(REDIS_KEY)).thenReturn(redisData);

        //when
        List<PuntoDeVenta> result = service.getAllPuntosDeVenta();

        //then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("CABA", result.get(0).getNombre());
        assertEquals(2L, result.get(1).getId());
        assertEquals("GBA_1", result.get(1).getNombre());
    }
}
