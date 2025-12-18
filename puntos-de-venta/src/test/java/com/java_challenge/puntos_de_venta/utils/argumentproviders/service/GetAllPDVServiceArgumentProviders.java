package com.java_challenge.puntos_de_venta.utils.argumentproviders.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;

public class GetAllPDVServiceArgumentProviders implements ArgumentsProvider {


    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        
        Map<Object, Object> redisData = new HashMap<>();
        redisData.put("1", "CABA");
        redisData.put("2", "GBA_1");

        List<PuntoDeVenta> dataResponse =
            List.of(
                new PuntoDeVenta(1L, "CABA"),
                new PuntoDeVenta(2L, "GBA_1")
            );

        return Stream.of(Arguments.of(redisData, dataResponse),
                Arguments.of(new HashMap<>(), (List.of()))
            );
    }
}
