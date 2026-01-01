package com.java_challenge.transporte;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.java_challenge.transporte.utils.argumentproviders.controller.CreateTransporteControllerArgumentProviders;
import com.java_challenge.transporte.utils.argumentproviders.controller.GetAllCostosControllerArgumentProviders;
import com.java_challenge.transporte.utils.argumentproviders.controller.UpdateTransporteControllerArgumentProviders;
import com.java_challenge.transporte.controller.TransporteController;
import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;
import com.java_challenge.transporte.service.TransporteService;

@ExtendWith(MockitoExtension.class)
class TransporteControllerTests {

    @InjectMocks
    private TransporteController controller;

    @Mock
    private TransporteService service;

    @ParameterizedTest
    @ArgumentsSource(GetAllCostosControllerArgumentProviders.class)
    void givenExistingData_whenGetAllTransporte_thenReturnsResponseEntity(List<Transporte> serviceResponse, ResponseEntity<List<Transporte>> expectedResponse) {

        //given
        given(service.getAllTransporte()).willReturn(serviceResponse);

        //when
        ResponseEntity<List<Transporte>> result = controller.getAllTransporte();

        //then
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponse.getBody().size(), result.getBody().size());
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @ArgumentsSource(CreateTransporteControllerArgumentProviders.class)
    void givenNewTransporte_whenCreateTransporte_thenReturnsResponseEntity(ResponseDTO serviceResponse, ResponseEntity<ResponseDTO> expectedResponse) {

        //given
        Transporte Transporte = new Transporte(12L, 1L, 9.0);
        given(service.createTransporte(Transporte)).willReturn(serviceResponse);

        //when
        ResponseEntity<ResponseDTO> result = controller.createTransporte(Transporte);

        //then
        assertEquals(expectedResponse, result);
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateTransporteControllerArgumentProviders.class)
    void givenTransporteUpdate_whenUpdateTransporte_thenReturnsResponseEntity(ResponseDTO serviceResponse, ResponseEntity<ResponseDTO> expectedResponse) {

        //given
        Transporte Transporte = new Transporte(2L, 5L, 2.0);
        given(service.updateTransporte(Transporte)).willReturn(serviceResponse);

        //when
        ResponseEntity<ResponseDTO> result = controller.updateTransporte(Transporte);

        //then
        assertEquals(expectedResponse, result);
    }

    @Test
    void givenId_whenDeleteTransporte_thenReturnsResponseDTO() {

        //given
        given(service.deleteTransporte(1L)).willReturn(new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Punto de Venta eliminado"));

        //when
        ResponseEntity<ResponseDTO> result = controller.deleteTransporte(1L);

        //then
        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Punto de Venta eliminado")), result);
    }
}
