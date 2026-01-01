package com.java_challenge.transporte.dtos;

import java.util.List;

import com.java_challenge.transporte.model.Transporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostoDeTransporteDetailsDTO {
    private String origen;
    private String destino;
    private List<Transporte> escalas;
    private double costoTotal;
}
