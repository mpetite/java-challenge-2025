package com.java_challenge.puntos_de_venta.dataload;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PuntosDeVentaRepository repository;

    public DataInitializer(PuntosDeVentaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            PuntosDeVenta pdv = new PuntosDeVenta();
            pdv.setId(1L);
            pdv.setZonas(Map.of(
                1, "CABA",
                2, "GBA_1",
                3, "GBA_2",
                4, "Santa Fe"
            ));
            repository.save(pdv);
        }
    }
}
