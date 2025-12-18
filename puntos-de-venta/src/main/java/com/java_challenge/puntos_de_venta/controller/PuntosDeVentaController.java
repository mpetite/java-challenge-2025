package com.java_challenge.puntos_de_venta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java_challenge.puntos_de_venta.dtos.ResponseDTO;
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
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createPuntosDeVenta(@RequestBody PuntoDeVenta puntoDeVenta) {
        
        ResponseDTO response = puntosDeVentaService.createPuntoDeVenta(puntoDeVenta);

        return ResponseEntity.status(response.getCode()).body(response);
    }
    //R
    @GetMapping("")
    public ResponseEntity<List<PuntoDeVenta>> getAllPuntosDeVenta() {
        return ResponseEntity.ok(puntosDeVentaService.getAllPuntosDeVenta());
    }
    //U

    //D

}