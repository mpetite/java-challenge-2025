package com.java_challenge.puntos_de_venta;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.java_challenge.puntos_de_venta.controller.PuntosDeVentaController;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;
import com.java_challenge.puntos_de_venta.service.PuntosDeVentaService;
import com.java_challenge.puntos_de_venta.utils.argumentproviders.controller.GetAllPDVControllerArgumentProviders;

@ExtendWith(MockitoExtension.class)
class PuntosDeVentaControllerTests {

    @InjectMocks
    private PuntosDeVentaController controller;

    @Mock
    private PuntosDeVentaService service;

    @ParameterizedTest
    @ArgumentsSource(GetAllPDVControllerArgumentProviders.class)
    void givenExistingData_whenGetAllPuntosDeVenta_thenReturnsList(List<PuntoDeVenta> data, ResponseEntity<List<PuntoDeVenta>> expectedResponse) {

        //given
        given(service.getAllPuntosDeVenta()).willReturn(data);

        //when
        ResponseEntity<List<PuntoDeVenta>> result = controller.getAllPuntosDeVenta();

        //then
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResponse.getBody().size(), result.getBody().size());
        assertEquals(expectedResponse, result);
    }
}
