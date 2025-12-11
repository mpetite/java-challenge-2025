package com.java_challenge.puntos_de_venta.repositories;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntosDeVentaRepository extends CrudRepository<PuntosDeVenta, Long> {

}
