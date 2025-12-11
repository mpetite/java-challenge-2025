package com.java_challenge.puntos_de_venta.controller;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;
import com.java_challenge.puntos_de_venta.service.PuntosDeVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/puntosdeventa")
public class PuntosDeVentaController {

    private PuntosDeVentaService puntosDeVentaService;

    public PuntosDeVentaController(PuntosDeVentaService puntosDeVentaService) {
        this.puntosDeVentaService = puntosDeVentaService;
    }
    //C

    //R
    @GetMapping("/")
    public Iterable<PuntosDeVenta> getAllPuntosDeVenta() {
        return puntosDeVentaService.getAllPuntosDeVenta();
    }
    //U

    //D

}