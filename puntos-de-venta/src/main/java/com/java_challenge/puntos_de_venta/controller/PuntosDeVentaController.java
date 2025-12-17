package com.java_challenge.puntos_de_venta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java_challenge.puntos_de_venta.model.PuntoDeVenta;
import com.java_challenge.puntos_de_venta.service.PuntosDeVentaService;

@RestController
@RequestMapping("/puntosdeventa")
public class PuntosDeVentaController {

    private final PuntosDeVentaService puntosDeVentaService;

    public PuntosDeVentaController(PuntosDeVentaService puntosDeVentaService) {
        this.puntosDeVentaService = puntosDeVentaService;
    }
    //C

    //R
    @GetMapping("")
    public ResponseEntity<List<PuntoDeVenta>> getAllPuntosDeVenta() {
        return Optional.of(puntosDeVentaService.getAllPuntosDeVenta()).map(ResponseEntity::ok)
        .orElseGet(()->ResponseEntity.notFound().build());
    }
    //U

    //D

}