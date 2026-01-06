package com.java_challenge.transporte.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java_challenge.transporte.dtos.CostoDeTransporteDetailsDTO;
import com.java_challenge.transporte.dtos.OrigenDestinoDTO;
import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;
import com.java_challenge.transporte.service.TransporteService;


@RestController
@RequestMapping("/transporte")
public class TransporteController {

    private final TransporteService transporteService;

    public TransporteController(TransporteService transporteService) {
        this.transporteService = transporteService;
    }

    //C
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createTransporte(@RequestBody Transporte transporte) {
        
        ResponseDTO response = transporteService.createTransporte(transporte);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    //R
    @PostMapping("")
    public ResponseEntity<CostoDeTransporteDetailsDTO> getCostoMinimoForTransporte(@RequestBody OrigenDestinoDTO origenDestino) {
        return ResponseEntity.ok(transporteService.getCostoMinimoForTransporte(origenDestino.getOrigenId(), origenDestino.getDestinoId()));
    }

    //U
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateTransporteCost(@RequestBody double costo) {
        
        ResponseDTO response = transporteService.updateTransporteCost(costo);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    //D
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteTransporte(@PathVariable Long id) {
        
        ResponseDTO response = transporteService.deleteTransporte(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}