package com.regata.config;

import com.regata.entities.*;
import com.regata.entities.Celda.TipoCelda;
import com.regata.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Inicializador de datos para poblar la base de datos con datos de ejemplo
 * al arrancar la aplicación.
 */
@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private BarcoRepository barcoRepository;

    @Autowired
    private MapaRepository mapaRepository;

    @Autowired
    private CeldaRepository celdaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar si no hay datos
        if (jugadorRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        System.out.println("Inicializando datos de ejemplo...");

        // Crear jugadores
        List<Jugador> jugadores = createJugadores();
        jugadorRepository.saveAll(jugadores);

        // Crear modelos
        List<Modelo> modelos = createModelos();
        modeloRepository.saveAll(modelos);

        // Crear mapa
        Mapa mapa = createMapa();
        mapaRepository.save(mapa);

        // Crear celdas del mapa
        List<Celda> celdas = createCeldas(mapa);
        celdaRepository.saveAll(celdas);

        // Crear barcos
        List<Barco> barcos = createBarcos(jugadores, modelos);
        barcoRepository.saveAll(barcos);

        System.out.println("Datos de ejemplo inicializados correctamente.");
        System.out.println("- Jugadores: " + jugadores.size());
        System.out.println("- Modelos: " + modelos.size());
        System.out.println("- Barcos: " + barcos.size());
        System.out.println("- Mapas: 1");
        System.out.println("- Celdas: " + celdas.size());
    }

    private List<Jugador> createJugadores() {
        return Arrays.asList(
            new Jugador("Carlos Mendoza", "carlos.mendoza@email.com"),
            new Jugador("Ana García", "ana.garcia@email.com"),
            new Jugador("Luis Rodríguez", "luis.rodriguez@email.com"),
            new Jugador("María López", "maria.lopez@email.com"),
            new Jugador("Pedro Sánchez", "pedro.sanchez@email.com"),
            new Jugador("Laura Martín", "laura.martin@email.com"),
            new Jugador("Diego Torres", "diego.torres@email.com"),
            new Jugador("Carmen Ruiz", "carmen.ruiz@email.com")
        );
    }

    private List<Modelo> createModelos() {
        return Arrays.asList(
            new Modelo("Velero Clásico", "#2196F3"),
            new Modelo("Yate de Lujo", "#FF5722"),
            new Modelo("Catamarán", "#4CAF50"),
            new Modelo("Barco de Vela", "#9C27B0"),
            new Modelo("Lancha Rápida", "#FF9800"),
            new Modelo("Velero de Competición", "#F44336"),
            new Modelo("Barco Pesquero", "#795548"),
            new Modelo("Yate Deportivo", "#607D8B")
        );
    }

    private Mapa createMapa() {
        return new Mapa("Campo de Regata Principal", 10, 10);
    }

    private List<Celda> createCeldas(Mapa mapa) {
        List<Celda> celdas = new ArrayList<>(Arrays.asList(
            // Línea de partida (fila 0)
            new Celda(mapa, 0, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 1, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 2, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 3, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 4, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 5, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 6, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 7, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 8, 0, TipoCelda.PARTIDA),
            new Celda(mapa, 9, 0, TipoCelda.PARTIDA),

            // Obstáculos (paredes) - Crear algunas islas
            new Celda(mapa, 2, 3, TipoCelda.PARED),
            new Celda(mapa, 3, 3, TipoCelda.PARED),
            new Celda(mapa, 2, 4, TipoCelda.PARED),
            new Celda(mapa, 3, 4, TipoCelda.PARED),

            new Celda(mapa, 6, 5, TipoCelda.PARED),
            new Celda(mapa, 7, 5, TipoCelda.PARED),
            new Celda(mapa, 6, 6, TipoCelda.PARED),
            new Celda(mapa, 7, 6, TipoCelda.PARED),

            new Celda(mapa, 1, 7, TipoCelda.PARED),
            new Celda(mapa, 2, 7, TipoCelda.PARED),
            new Celda(mapa, 1, 8, TipoCelda.PARED),

            // Línea de meta (fila 9)
            new Celda(mapa, 0, 9, TipoCelda.META),
            new Celda(mapa, 1, 9, TipoCelda.META),
            new Celda(mapa, 2, 9, TipoCelda.META),
            new Celda(mapa, 3, 9, TipoCelda.META),
            new Celda(mapa, 4, 9, TipoCelda.META),
            new Celda(mapa, 5, 9, TipoCelda.META),
            new Celda(mapa, 6, 9, TipoCelda.META),
            new Celda(mapa, 7, 9, TipoCelda.META),
            new Celda(mapa, 8, 9, TipoCelda.META),
            new Celda(mapa, 9, 9, TipoCelda.META)
        ));

        // Agregar algunas celdas de agua específicas para completar el mapa
        // (Las celdas no definidas se considerarán agua por defecto en la vista)
        for (int x = 0; x < 10; x++) {
            for (int y = 1; y < 9; y++) {
                // Solo agregar si no es una celda ya definida
                final int finalX = x;
                final int finalY = y;
                boolean exists = celdas.stream().anyMatch(c -> c.getCoordX() == finalX && c.getCoordY() == finalY);
                if (!exists) {
                    celdas.add(new Celda(mapa, finalX, finalY, TipoCelda.AGUA));
                }
            }
        }

        return celdas;
    }

    private List<Barco> createBarcos(List<Jugador> jugadores, List<Modelo> modelos) {
        return Arrays.asList(
            // Barcos en la línea de partida
            new Barco(jugadores.get(0), modelos.get(0), 1, 0, 0, 0),
            new Barco(jugadores.get(1), modelos.get(1), 3, 0, 0, 0),
            new Barco(jugadores.get(2), modelos.get(2), 5, 0, 0, 0),
            new Barco(jugadores.get(3), modelos.get(3), 7, 0, 0, 0),
            
            // Barcos en diferentes posiciones del campo
            new Barco(jugadores.get(4), modelos.get(4), 0, 2, 0, 0),
            new Barco(jugadores.get(5), modelos.get(5), 4, 3, 0, 0),
            new Barco(jugadores.get(6), modelos.get(6), 8, 4, 0, 0),
            new Barco(jugadores.get(7), modelos.get(7), 5, 7, 0, 0)
        );
    }
}