package com.java_challenge.puntos_de_venta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;

import com.java_challenge.puntos_de_venta.controller.PuntosDeVentaController;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;
import com.java_challenge.puntos_de_venta.utils.argumentproviders.GetAllPDVArgumentProviders;

@ExtendWith(MockitoExtension.class)
public class PuntosDeVentaControllerTests {

    private static final String REDIS_KEY = "challenge:puntos-de-venta:1";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private PuntosDeVentaController controller;

    private Map<Object, Object> redisData;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        redisData = new HashMap<>();
        redisData.put("1", "CABA");
        redisData.put("2", "GBA_1");
    }

    @ParameterizedTest
    @ArgumentsSource(GetAllPDVArgumentProviders.class)
    void givenExistingData_whenGetAllPuntosDeVenta_thenReturnsList(Map<Object, Object> data, ResponseEntity<List<PuntoDeVenta>> expectedResponse) {

        //given
        when(hashOperations.entries(REDIS_KEY)).thenReturn(redisData);

        //when
        ResponseEntity<List<PuntoDeVenta>> result = controller.getAllPuntosDeVenta();

        //then
        assertTrue(result.getStatusCode().is2xxSuccessful());//status code check
        assertEquals(expectedResponse.getBody().size(), result.getBody().size());//body size check
        assertEquals(expectedResponse, result);
    }
}
