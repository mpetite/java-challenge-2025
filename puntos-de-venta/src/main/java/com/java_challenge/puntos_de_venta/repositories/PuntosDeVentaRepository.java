package com.java_challenge.puntos_de_venta.repositories;

import com.java_challenge.puntos_de_venta.model.PuntosDeVenta;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;


@Repository
@RedisHash
public interface PuntosDeVentaRepository extends KeyValueRepository<PuntosDeVenta, Long> {

}
