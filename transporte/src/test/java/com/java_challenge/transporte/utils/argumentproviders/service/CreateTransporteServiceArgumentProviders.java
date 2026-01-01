package com.java_challenge.transporte.utils.argumentproviders.service;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;

public class CreateTransporteServiceArgumentProviders implements ArgumentsProvider {


    @Override
    @Deprecated
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        Transporte buenTransporte = new Transporte(11L, 1L, 150.0);
        ResponseDTO buenaRespuesta = new ResponseDTO(201, "Punto de Venta creado");
        Transporte malTransporte = new Transporte(0L, 1L, 0.0);
        ResponseDTO malaRespuesta = new ResponseDTO(500, "Error creando Punto de Venta: ");
        return Stream.of(
            Arguments.of(buenTransporte, buenaRespuesta),
            Arguments.of(malTransporte, malaRespuesta)
        );
    }
}
