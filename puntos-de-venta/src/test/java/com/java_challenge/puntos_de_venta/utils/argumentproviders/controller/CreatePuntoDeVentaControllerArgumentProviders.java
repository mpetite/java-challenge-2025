package com.java_challenge.puntos_de_venta.utils.argumentproviders.controller;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;

public class CreatePuntoDeVentaControllerArgumentProviders implements ArgumentsProvider {


    @Override
    @Deprecated
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        ResponseDTO createdResponse = new ResponseDTO(201, "Punto de Venta creado");
        ResponseDTO notCreatedResponse = new ResponseDTO(500, "Error creando Punto de Venta: ");
        return Stream.of(
            Arguments.of(createdResponse, ResponseEntity.status(createdResponse.getCode()).body(createdResponse)),
            Arguments.of(notCreatedResponse, ResponseEntity.status(notCreatedResponse.getCode()).body(notCreatedResponse))
        );
    }
}
