package com.java_challenge.transporte.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrigenDestinoDTO {
    private Long origenId;
    private Long destinoId;
}
