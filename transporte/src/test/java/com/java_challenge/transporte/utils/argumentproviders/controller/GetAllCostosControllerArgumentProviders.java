package com.java_challenge.transporte.utils.argumentproviders.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import com.java_challenge.transporte.model.Transporte;

public class GetAllCostosControllerArgumentProviders implements ArgumentsProvider {


    @Override
    @Deprecated
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        
       List<Transporte> serviceResponse = new ArrayList<>();
        serviceResponse.add(new Transporte(1L,1L,0.0));
        serviceResponse.add(new Transporte(2L,2L,0.0));

        ResponseEntity<List<Transporte>> expectedResponse =
            ResponseEntity.ok(serviceResponse);

        return Stream.of(Arguments.of(serviceResponse, expectedResponse),
                Arguments.of(new ArrayList<>(), ResponseEntity.ok(new ArrayList<>()))
            );
    }
}
