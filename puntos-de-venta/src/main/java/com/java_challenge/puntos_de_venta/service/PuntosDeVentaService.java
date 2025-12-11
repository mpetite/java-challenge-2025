package com.java_challenge.puntos_de_venta.service;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;
import com.java_challenge.puntos_de_venta.repositories.PuntosDeVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PuntosDeVentaService {

    private final PuntosDeVentaRepository puntosDeVentaRepository;

    public PuntosDeVentaService(PuntosDeVentaRepository puntosDeVentaRepository) {
        this.puntosDeVentaRepository = puntosDeVentaRepository;
    }

    //C

    //R
    public Iterable<PuntosDeVenta> getAllPuntosDeVenta() {
        return puntosDeVentaRepository.findAll();
    }
    //U

    //D

}
