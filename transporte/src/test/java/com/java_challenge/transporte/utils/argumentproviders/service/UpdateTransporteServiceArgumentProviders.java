package com.java_challenge.transporte.utils.argumentproviders.service;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;

public class UpdateTransporteServiceArgumentProviders implements ArgumentsProvider {


    @Override
    @Deprecated
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        Transporte buenTransporte = new Transporte(2L, 2L, 0.0);
        ResponseDTO buenaRespuesta = new ResponseDTO(HttpStatus.OK.value(), "Punto de Venta actualizado");
        Transporte malTransporte = new Transporte(0L, 1L, -1.0);
        ResponseDTO malaRespuesta = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error actualizando Punto de Venta: ");
        return Stream.of(
            Arguments.of(buenTransporte, buenaRespuesta),
            Arguments.of(malTransporte, malaRespuesta)
        );
    }
}
