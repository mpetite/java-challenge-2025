package com.java_challenge.puntos_de_venta.utils.argumentproviders.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import com.java_challenge.transporte.model.PuntoDeVenta;

public class GetAllPDVControllerArgumentProviders implements ArgumentsProvider {


    @Override
    @Deprecated
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        
       List<PuntoDeVenta> serviceResponse = new ArrayList<>();
        serviceResponse.add(new PuntoDeVenta(1L, "CABA"));
        serviceResponse.add(new PuntoDeVenta(2L, "GBA_1"));

        ResponseEntity<List<PuntoDeVenta>> expectedResponse =
            ResponseEntity.ok(serviceResponse);

        return Stream.of(Arguments.of(serviceResponse, expectedResponse),
                Arguments.of(new ArrayList<>(), ResponseEntity.ok(new ArrayList<>()))
            );
    }
}
