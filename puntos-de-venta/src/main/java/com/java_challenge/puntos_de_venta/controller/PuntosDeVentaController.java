package com.java_challenge.puntos_de_venta.controller;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;
import com.java_challenge.puntos_de_venta.service.PuntosDeVentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/puntosdeventa")
public class PuntosDeVentaController {

    private PuntosDeVentaService puntosDeVentaService;

    public PuntosDeVentaController(PuntosDeVentaService puntosDeVentaService) {
        this.puntosDeVentaService = puntosDeVentaService;
    }
    //C

    //R
    @GetMapping("/")
    public ResponseEntity<List<PuntosDeVenta>> getAllPuntosDeVenta() {
        return Optional.of(puntosDeVentaService.getAllPuntosDeVenta()).map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }
    //U

    //D

}