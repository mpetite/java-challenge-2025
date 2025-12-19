package com.java_challenge.puntos_de_venta.utils.argumentproviders.service;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;

public class CreatePuntoDeVentaServiceArgumentProviders implements ArgumentsProvider {


    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        PuntoDeVenta buenPuntoDeVenta = new PuntoDeVenta(11L, "Neuquén");
        ResponseDTO buenaRespuesta = new ResponseDTO(201, "Punto de Venta creado");
        PuntoDeVenta malPuntoDeVenta = new PuntoDeVenta(0L, "Isla Roshi");
        ResponseDTO malaRespuesta = new ResponseDTO(500, "Error creando Punto de Venta: ");
        return Stream.of(
            Arguments.of(buenPuntoDeVenta, buenaRespuesta),
            Arguments.of(malPuntoDeVenta, malaRespuesta)
        );
    }
}
