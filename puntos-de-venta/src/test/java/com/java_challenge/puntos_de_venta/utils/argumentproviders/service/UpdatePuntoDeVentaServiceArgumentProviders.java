package com.java_challenge.puntos_de_venta.utils.argumentproviders.service;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;
import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;

public class UpdatePuntoDeVentaServiceArgumentProviders implements ArgumentsProvider {


    @Override
    @Deprecated
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        PuntoDeVenta buenPuntoDeVenta = new PuntoDeVenta(2L, "Neuquén");
        ResponseDTO buenaRespuesta = new ResponseDTO(HttpStatus.OK.value(), "Punto de Venta actualizado");
        PuntoDeVenta malPuntoDeVenta = new PuntoDeVenta(0L, "Isla Roshi");
        ResponseDTO malaRespuesta = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error actualizando Punto de Venta: ");
        return Stream.of(
            Arguments.of(buenPuntoDeVenta, buenaRespuesta),
            Arguments.of(malPuntoDeVenta, malaRespuesta)
        );
    }
}
