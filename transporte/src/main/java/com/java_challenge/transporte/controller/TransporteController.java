package com.java_challenge.transporte.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.Transporte;
import com.java_challenge.transporte.service.TransporteService;


@RestController
@RequestMapping("/transporte")
public class TransporteController {

    private final TransporteService TransporteService;

    public TransporteController(TransporteService TransporteService) {
        this.TransporteService = TransporteService;
    }

    //C
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createTransporte(@RequestBody Transporte Transporte) {
        
        ResponseDTO response = TransporteService.createTransporte(Transporte);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    //R
    @GetMapping("")
    public ResponseEntity<List<Transporte>> getAllPuntosDeVenta() {
        return ResponseEntity.ok(TransporteService.getAllPuntosDeVenta());
    }

    //U
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateTransporte(@RequestBody Transporte Transporte) {
        
        ResponseDTO response = TransporteService.updateTransporte(Transporte);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    //D
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteTransporte(@PathVariable Long id) {
        
        ResponseDTO response = TransporteService.deleteTransporte(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}