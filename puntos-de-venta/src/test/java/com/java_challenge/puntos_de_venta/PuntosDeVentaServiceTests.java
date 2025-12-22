package com.java_challenge.puntos_de_venta;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;
import com.java_challenge.puntos_de_venta.service.PuntosDeVentaService;
import com.java_challenge.puntos_de_venta.utils.argumentproviders.service.CreatePuntoDeVentaServiceArgumentProviders;
import com.java_challenge.puntos_de_venta.utils.argumentproviders.service.GetAllPDVServiceArgumentProviders;
import com.java_challenge.puntos_de_venta.utils.argumentproviders.service.UpdatePuntoDeVentaServiceArgumentProviders;

@ExtendWith(MockitoExtension.class)
class PuntosDeVentaServiceTests {

    private static final String PUNTOS_DE_VENTA_KEY = "challenge:puntos-de-venta:1";

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

    @ParameterizedTest
    @ArgumentsSource(GetAllPDVServiceArgumentProviders.class)
    void givenExistingData_whenGetAllPuntosDeVenta_thenReturnsList(Map<Object, Object> data, List<PuntoDeVenta> expectedResponse) {

        //given
        when(hashOperations.entries(PUNTOS_DE_VENTA_KEY)).thenReturn(data);

        //when
        List<PuntoDeVenta> result = service.getAllPuntosDeVenta();

        //then
        assertEquals(expectedResponse.size(), result.size());
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @ArgumentsSource(CreatePuntoDeVentaServiceArgumentProviders.class)
    void givenNewPuntoDeVenta_whenCreatePuntosDeVenta_thenReturnsResponseDTO(PuntoDeVenta puntoDeVenta, ResponseDTO expectedResponse) {

        //given
        if(puntoDeVenta.getId() == 0)
            doThrow(new RuntimeException("")).when(hashOperations).put(PUNTOS_DE_VENTA_KEY, puntoDeVenta.getId().toString(), puntoDeVenta.getNombre());
        

        //when
        ResponseDTO result = service.createPuntoDeVenta(puntoDeVenta);

        //then
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @ArgumentsSource(UpdatePuntoDeVentaServiceArgumentProviders.class)
    void givenPuntoDeVentaUpdate_whenUpdatePuntosDeVenta_thenReturnsResponseDTO(PuntoDeVenta puntoDeVenta, ResponseDTO expectedResponse) {

        //given
        if(puntoDeVenta.getId() == 0)
            doThrow(new RuntimeException("")).when(hashOperations).put(PUNTOS_DE_VENTA_KEY, puntoDeVenta.getId().toString(), puntoDeVenta.getNombre());
        

        //when
        ResponseDTO result = service.updatePuntoDeVenta(puntoDeVenta);

        //then
        assertEquals(expectedResponse, result);
    }

    @Test
    void givenId_whenDeletePuntosDeVenta_thenReturnsResponseDTO() {

        //when
        ResponseDTO result = service.deletePuntoDeVenta(1L);

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getCode());
    }
}
