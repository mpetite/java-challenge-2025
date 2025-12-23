package com.java_challenge.transporte;


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

import com.java_challenge.transporte.utils.argumentproviders.service.CreateTransporteServiceArgumentProviders;
import com.java_challenge.transporte.utils.argumentproviders.service.GetAllPDVServiceArgumentProviders;
import com.java_challenge.transporte.utils.argumentproviders.service.UpdateTransporteServiceArgumentProviders;
import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;
import com.java_challenge.transporte.service.TransporteService;

@ExtendWith(MockitoExtension.class)
class TransporteServiceTests {

    private static final String PUNTOS_DE_VENTA_KEY = "challenge:puntos-de-venta:1";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private TransporteService service;

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
    void givenExistingData_whenGetAllTransporte_thenReturnsList(Map<Object, Object> data, List<Transporte> expectedResponse) {

        //given
        when(hashOperations.entries(PUNTOS_DE_VENTA_KEY)).thenReturn(data);

        //when
        List<Transporte> result = service.getAllTransporte();

        //then
        assertEquals(expectedResponse.size(), result.size());
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @ArgumentsSource(CreateTransporteServiceArgumentProviders.class)
    void givenNewTransporte_whenCreateTransporte_thenReturnsResponseDTO(Transporte Transporte, ResponseDTO expectedResponse) {

        //given
        if(Transporte.getId() == 0)
            doThrow(new RuntimeException("")).when(hashOperations).put(PUNTOS_DE_VENTA_KEY, Transporte.getId().toString(), Transporte.getNombre());
        

        //when
        ResponseDTO result = service.createTransporte(Transporte);

        //then
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateTransporteServiceArgumentProviders.class)
    void givenTransporteUpdate_whenUpdateTransporte_thenReturnsResponseDTO(Transporte Transporte, ResponseDTO expectedResponse) {

        //given
        if(Transporte.getId() == 0)
            doThrow(new RuntimeException("")).when(hashOperations).put(PUNTOS_DE_VENTA_KEY, Transporte.getId().toString(), Transporte.getNombre());
        

        //when
        ResponseDTO result = service.updateTransporte(Transporte);

        //then
        assertEquals(expectedResponse, result);
    }

    @Test
    void givenId_whenDeleteTransporte_thenReturnsResponseDTO() {

        //when
        ResponseDTO result = service.deleteTransporte(1L);

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getCode());
    }
}
