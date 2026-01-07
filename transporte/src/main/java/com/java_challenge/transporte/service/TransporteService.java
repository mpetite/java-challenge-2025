package com.java_challenge.transporte.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            List<PuntoDeVenta> puntosDeVenta = resp.getBody() == null ? Collections.emptyList() : Arrays.asList(resp.getBody());
            System.out.println("Fetched puntos de venta: " + puntosDeVenta);
            PuntoDeVenta origenPunto = findPuntoFromId(puntosDeVenta, origenId);
            System.out.println("Origen puntos de venta: " + origenPunto);   
            PuntoDeVenta destinoPunto = findPuntoFromId(puntosDeVenta, destinoId);
            System.out.println("Destino puntos de venta: " + destinoPunto);

            if (origenId == destinoId) 
                return mismoOrigenDestino(origenId, destinoId, puntosDeVenta);

            List<Transporte> allTransportes = getAllTransportes();
            Map<PuntoDeVenta, List<Transporte>> graph = getMappedGraph(origenPunto, allTransportes);
            CostoDeTransporteDetailsDTO resultDTO = new CostoDeTransporteDetailsDTO();
            allTransportes.stream()
                .filter(transporte -> transporte.getOrigen().equals(origenId) && transporte.getDestino().equals(destinoId))
                .findFirst()
                .ifPresent(transporte -> {
                    resultDTO.setOrigen(origenPunto.getNombre());
                    resultDTO.setDestino(destinoPunto.getNombre());
                    resultDTO.setEscalas(List.of());
                    resultDTO.setCostoTotal(transporte.getCosto());
                });
                    //SETUP (esta ok)
                    // init adjacency list
                    System.out.println("EMPIEZA SETUP");
                    Map<Long, List<Transporte>> allAdjacencies = new HashMap<>();
                    for (Transporte transporte : allTransportes) {
                        allAdjacencies.computeIfAbsent(transporte.getOrigen(), v -> new java.util.ArrayList<>()).add(transporte); //me parece que aca hay que corregir filtrando por los transportes que tengan el key como origen
                    }
                    System.out.println("Adjacencies: " + allAdjacencies);

                    // Dijkstra using a priority queue for efficiency
                    Map<Long, Double> minCostByNode = new HashMap<>();
                    Map<Long, Transporte> previousTransportByNode = new HashMap<>();
                    //inicializas el graph con todos los puntos con costo infinito
                    for (PuntoDeVenta punto : puntosDeVenta) 
                        minCostByNode.put(punto.getId(), Double.POSITIVE_INFINITY);
                    //seteas el origen con costo 0
                    minCostByNode.put(origenId, 0.0);
                    System.out.println("Min distances initialized: " + minCostByNode);

                    //inicializas una PQ con comparador de double
                    java.util.PriorityQueue<Map.Entry<Long, Double>> priorityQueue =
                        new java.util.PriorityQueue<>(java.util.Comparator.comparingDouble(Map.Entry::getValue));
                    //le agregas el origen con costo 0
                    priorityQueue.add(new java.util.AbstractMap.SimpleEntry<>(origenId, 0.0));
                    System.out.println("Priority queue initialized: " + priorityQueue);

                    System.out.println("EMPIEZA EL PROCESO");
                    // PROCESO
                    while (!priorityQueue.isEmpty()) {
                        Map.Entry<Long, Double> entry = priorityQueue.poll(); //es un especie de pop
                        System.out.println("Entry polleada: " + entry);
                        System.out.println("PQ dsps de poll: " + priorityQueue);
                        Long currentNodeId = entry.getKey();
                        double currentKnownCost = entry.getValue(); // 0.0 para el primer uso

                        // Ignore stale queue entries ???
                        if (currentKnownCost > minCostByNode.getOrDefault(currentNodeId, Double.POSITIVE_INFINITY)) continue;
                        //if (currentNodeId.equals(destinoId)) break; // esto esta mal

                        //buscas los adjacentes del current node
                        List<Transporte> currNodeAdjacencies = allAdjacencies.getOrDefault(currentNodeId, List.of());
                        //para cada uno
                        for (Transporte edge : currNodeAdjacencies) {
                            //guarda el id del adjaciente
                            Long neighborId = edge.getDestino();
                            //calcula el costo total desde el origen hacia el punto adjacente
                            double candidateCost = currentKnownCost + edge.getCosto();
                            //si el costo total desde el origen hacia el punto adjacente es menor al costo minimo registrado hasta entonces para ese destino
                            if (candidateCost < minCostByNode.getOrDefault(neighborId, Double.POSITIVE_INFINITY)) {
                                minCostByNode.put(neighborId, candidateCost);
                                previousTransportByNode.put(neighborId, edge);
                                priorityQueue.add(new java.util.AbstractMap.SimpleEntry<>(neighborId, candidateCost));
                            }
                        }
                    }

                    double finalDistance = minCostByNode.getOrDefault(destinoId, Double.POSITIVE_INFINITY);
                    if (Double.isInfinite(finalDistance)) {
                        return new CostoDeTransporteDetailsDTO(
                            origenPunto == null ? String.valueOf(origenId) : origenPunto.getNombre(),
                            destinoPunto == null ? String.valueOf(destinoId) : destinoPunto.getNombre(),
                            List.of(),
                            -1.0
                        );
                    }

                    // Reconstruct transport sequence from origin -> destination
                    java.util.LinkedList<Transporte> transportPath = new java.util.LinkedList<>();
                    Long cursor = destinoId;
                    while (previousTransportByNode.containsKey(cursor)) {
                        Transporte stepTransport = previousTransportByNode.get(cursor);
                        transportPath.addFirst(stepTransport);
                        cursor = stepTransport.getOrigen();
                    }

                    resultDTO.setOrigen(origenPunto == null ? String.valueOf(origenId) : origenPunto.getNombre());
                    resultDTO.setDestino(destinoPunto == null ? String.valueOf(destinoId) : destinoPunto.getNombre());
                    resultDTO.setEscalas(transportPath);
                    resultDTO.setCostoTotal(finalDistance);
            
            // TODO: implement pathfinding using puntos and transportes; returning placeholder for now
            return resultDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching puntos de venta: " + e.getMessage());
        }
    }

    //U
    @CacheEvict(value = TRANSPORTE_CACHE, allEntries = true)
    public ResponseDTO updateTransporteCost(double costo) {
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

    private Map<PuntoDeVenta, List<Transporte>> getMappedGraph(PuntoDeVenta origenPunto, List<Transporte> transportes) {
        Map<PuntoDeVenta, List<Transporte>> graph = new HashMap<>();

        graph.put(origenPunto, transportes.stream()
            .filter(transporte -> transporte.getOrigen().equals(origenPunto.getId()))
                .toList());

        return graph;
    }

    private List<PuntoDeVenta> findUnsettled(List<Transporte> allTransportes, List<PuntoDeVenta> puntosDeVenta, Long origenId) {
        return allTransportes.stream()
                        .filter(transporte -> transporte.getOrigen().equals(origenId))
                        .map(transporte -> puntosDeVenta.stream()
                            .filter(punto -> punto.getId().equals(transporte.getDestino())).toList()).flatMap(List::stream).toList();        
    }

    private PuntoDeVenta findPuntoFromId(List<PuntoDeVenta> puntosDeVenta, Long id) {
        return puntosDeVenta.stream()
            .filter(punto -> punto.getId().equals(id))
            .findFirst().orElse(null);
    }
}