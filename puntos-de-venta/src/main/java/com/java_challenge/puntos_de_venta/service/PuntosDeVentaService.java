package com.java_challenge.puntos_de_venta.service;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;
import com.java_challenge.puntos_de_venta.repositories.PuntosDeVentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntosDeVentaService {

    private PuntosDeVentaRepository puntosDeVentaRepository;

    public PuntosDeVentaService(PuntosDeVentaRepository puntosDeVentaRepository) {
        this.puntosDeVentaRepository = puntosDeVentaRepository;
    }

    //C

    //R
    public List<PuntosDeVenta> getAllPuntosDeVenta() {
        return puntosDeVentaRepository.findAllPuntosDeVenta();
    }
    //U

    //D

}
