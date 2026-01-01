package com.java_challenge.transporte.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.java_challenge.transporte.dtos.CostoDeTransporteDetailsDTO;
import com.java_challenge.transporte.dtos.ResponseDTO;
import com.java_challenge.transporte.model.PuntoDeVenta;
import com.java_challenge.transporte.model.Transporte;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_CACHE;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_CREATED_MESSAGE;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_DELETED_MESSAGE;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_ENTRY_COSTO_INDEX;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_ENTRY_DESTINO_INDEX;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_ENTRY_ORIGEN_INDEX;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_KEY;
import static com.java_challenge.transporte.utils.CommonConstants.TRANSPORTE_UPDATED_MESSAGE;

@Service
public class TransporteService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TransporteService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final RestClient restClient = RestClient.create();

    //C
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO createTransporte(Transporte transporte) {
        try {

            if (transporteExists(transporte)) 
                return new ResponseDTO(HttpStatus.CONFLICT.value(), "El transporte entre esos dos puntos ya existe, no se puede crear duplicado.");

            if(transporte.getCosto() < 0) {
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "El costo del transporte no puede ser negativo.");
            } else if (transporte.getOrigen().equals(transporte.getDestino()) && transporte.getCosto() != 0) {
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "El costo del transporte entre el mismo origen y destino debe ser 0.");
            }

            int existingEntriesCount = redisTemplate.opsForHash().size(TRANSPORTE_KEY).intValue();
            redisTemplate.opsForHash().put(TRANSPORTE_KEY, Integer.toString(existingEntriesCount + 1), transporteToEntryValue(transporte));
            return new ResponseDTO(HttpStatus.CREATED.value(), TRANSPORTE_CREATED_MESSAGE);
        }
        catch(Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creando Costo de Transporte: " + e.getMessage());
        }
    }

    //R

    @Cacheable(value = TRANSPORTE_CACHE)
    public List<Transporte> getCostosDirectosForOrigen(int origenId) {
        /*
        Logica a agregar:
        1. Filtrar los transportes por origenId
        */

        return redisTemplate.opsForHash()
            .entries(TRANSPORTE_KEY)
            .entrySet().stream()
            .map(entry ->{
                String[] parsedTransporteValue = parseTransporteValue(entry);
                return new Transporte(Long.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_ORIGEN_INDEX]),
                Long.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_DESTINO_INDEX]),
                Double.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_COSTO_INDEX]));
            }).toList();
    }

    @Cacheable(value = TRANSPORTE_CACHE)
    public CostoDeTransporteDetailsDTO getCostoMinimoForTransporte(Long origenId, Long destinoId) {
        
        try {
            String url = "http://localhost:8081/puntosdeventa";
            ResponseEntity<PuntoDeVenta[]> resp = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(PuntoDeVenta[].class);
            List<PuntoDeVenta> puntosDeVenta = (resp.getBody() == null) ? Collections.emptyList() : Arrays.asList(resp.getBody());

            if (origenId == destinoId) 
                return mismoOrigenDestino(origenId, destinoId, puntosDeVenta);
            
            return getAllTransportes().stream()
                .filter(transporte -> 
                    transporte.getOrigen().equals(origenId) && 
                    transporte.getDestino().equals(destinoId)
                ).findFirst()
                .orElse(transporte ->
                    findMatchingWithEscalas(origenId, destinoId, transporte, puntosDeVenta)
                ).map

            // TODO: implement pathfinding using puntos and transportes; returning placeholder for now
            return new CostoDeTransporteDetailsDTO(String.valueOf(origenId), String.valueOf(destinoId), List.of(), 0.0);
        } catch (Exception e) {
            return new CostoDeTransporteDetailsDTO(String.valueOf(origenId), String.valueOf(destinoId), List.of(), -1.0);
        }
    }

    //U
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO updateTransporteCost(int costo) {
        try {
            //redisTemplate.opsForHash().put(TRANSPORTE_KEY, costo.toString(), transporte.getNombre());
            return new ResponseDTO(HttpStatus.OK.value(), TRANSPORTE_UPDATED_MESSAGE);
        }
        catch(Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error actualizando Costo de Transporte: " + e.getMessage());
        }
    }
    
    //D
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO deleteTransporte(Long id) {
            redisTemplate.opsForHash().delete(TRANSPORTE_KEY, id.toString());
            return new ResponseDTO(HttpStatus.NO_CONTENT.value(), TRANSPORTE_DELETED_MESSAGE);
    }

    //Utils
    private String[] parseTransporteValue(java.util.Map.Entry<Object, Object> entry) {
        String value = entry.getValue().toString();
        return value.split(",");
    }

    private String transporteToEntryValue(Transporte transporte) {
        return transporte.getOrigen() + "," + transporte.getDestino() + "," + transporte.getCosto();
    }

    private boolean transporteExists(Transporte transporte) {
        return redisTemplate.opsForHash()
            .entries(TRANSPORTE_KEY)
            .entrySet().stream()
            .map(entry ->{
                String[] parsedTransporteValue = parseTransporteValue(entry);
                return new Transporte(Long.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_ORIGEN_INDEX]),
                Long.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_DESTINO_INDEX]),
                Double.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_COSTO_INDEX]));
            })
            .anyMatch(existingTransporte -> 
                existingTransporte.getOrigen().equals(transporte.getOrigen()) &&
                existingTransporte.getDestino().equals(transporte.getDestino())
            );
    }

    private CostoDeTransporteDetailsDTO mismoOrigenDestino(Long origenId, Long destinoId, List<PuntoDeVenta> puntosDeVenta) {
        
        return puntosDeVenta.stream()
            .filter(punto -> punto.getId().equals(origenId))
            .findFirst()
            .map(punto -> new CostoDeTransporteDetailsDTO(
                punto.getNombre(),
                punto.getNombre(),
                List.of(),
                0.0
            ))
        .orElse(new CostoDeTransporteDetailsDTO(
            "Sin definir",
            "Sin definir",
            List.of(),
            0.0
        ));
    }

    private List<Transporte> getAllTransportes() {

    return redisTemplate.opsForHash()
        .entries(TRANSPORTE_KEY)
        .entrySet().stream()
        .map(entry ->{
            String[] parsedTransporteValue = parseTransporteValue(entry);
            return new Transporte(Long.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_ORIGEN_INDEX]),
            Long.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_DESTINO_INDEX]),
            Double.valueOf(parsedTransporteValue[TRANSPORTE_ENTRY_COSTO_INDEX]));
        }).toList();
    }
}