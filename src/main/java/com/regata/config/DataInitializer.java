package com.regata.config;

import com.regata.entities.*;
import com.regata.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Autowired
    private ModeloRepository modeloRepository;
    
    @Autowired
    private MapaRepository mapaRepository;
    
    @Autowired
    private CeldaRepository celdaRepository;
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Solo inicializar si la base de datos está vacía
        if (jugadorRepository.count() == 0) {
            System.out.println("Inicializando base de datos con datos de prueba...");
            
            // 1. Crear 5 jugadores
            List<Jugador> jugadores = crearJugadores();
            
            // 2. Crear 10 modelos de barcos
            List<Modelo> modelos = crearModelos();
            
            // 3. Crear un mapa jugable
            crearMapaJugable();
            
            // 4. Crear 50 barcos (10 por jugador)
            crearBarcos(jugadores, modelos);
            
            System.out.println("Base de datos inicializada correctamente:");
            System.out.println("- " + jugadores.size() + " jugadores creados");
            System.out.println("- " + modelos.size() + " modelos de barcos creados");
            System.out.println("- 1 mapa jugable creado con " + celdaRepository.count() + " celdas");
            System.out.println("- " + barcoRepository.count() + " barcos creados");
        } else {
            System.out.println("La base de datos ya contiene datos. Omitiendo inicialización.");
        }
    }
    
    private List<Jugador> crearJugadores() {
        List<Jugador> jugadores = new ArrayList<>();
        String[] nombres = {"Santiago", "María", "Carlos", "Ana", "Diego"};
        
        for (int i = 0; i < nombres.length; i++) {
            Jugador jugador = new Jugador();
            jugador.setNombre(nombres[i]);
            jugador.setEmail(nombres[i].toLowerCase() + "@regata.com");
            jugadores.add(jugadorRepository.save(jugador));
        }
        
        return jugadores;
    }
    
    private List<Modelo> crearModelos() {
        List<Modelo> modelos = new ArrayList<>();
        String[] nombresModelos = {
            "Velero Clásico", "Catamarán Rápido", "Yate de Lujo", "Barco Pesquero", 
            "Lancha Deportiva", "Velero de Competición", "Barco Pirata", "Crucero",
            "Barco de Vela", "Embarcación Ligera"
        };
        String[] colores = {
            "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF",
            "#00FFFF", "#FFA500", "#800080", "#008000", "#000080"
        };
        
        for (int i = 0; i < nombresModelos.length; i++) {
            Modelo modelo = new Modelo();
            modelo.setNombre(nombresModelos[i]);
            modelo.setColor(colores[i]);
            modelos.add(modeloRepository.save(modelo));
        }
        
        return modelos;
    }
    
    private Mapa crearMapaJugable() {
        // Crear mapa de 10x10
        Mapa mapa = new Mapa();
        mapa.setNombre("Mapa Principal de Regata");
        mapa.setTamFilas(10);
        mapa.setTamColumnas(10);
        mapa = mapaRepository.save(mapa);
        
        // Crear celdas del mapa
        List<Celda> celdas = new ArrayList<>();
        
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Celda celda = new Celda();
                celda.setMapa(mapa);
                celda.setCoordX(x);
                celda.setCoordY(y);
                
                // Definir tipos de celdas para hacer el mapa jugable
                if (y == 0) {
                    // Primera fila: posiciones de partida
                    celda.setTipo(Celda.TipoCelda.PARTIDA);
                } else if (y == 9) {
                    // Última fila: meta
                    celda.setTipo(Celda.TipoCelda.META);
                } else if ((x == 0 || x == 9) && y > 1 && y < 8) {
                    // Paredes en los bordes laterales (excepto cerca de partida y meta)
                    celda.setTipo(Celda.TipoCelda.PARED);
                } else if (y == 4 && x >= 3 && x <= 6) {
                    // Obstáculos en el medio para hacer el juego más interesante
                    celda.setTipo(Celda.TipoCelda.PARED);
                } else {
                    // El resto son celdas de agua navegables
                    celda.setTipo(Celda.TipoCelda.AGUA);
                }
                
                celdas.add(celda);
            }
        }
        
        celdaRepository.saveAll(celdas);
        return mapa;
    }
    
    private void crearBarcos(List<Jugador> jugadores, List<Modelo> modelos) {
        List<Barco> barcos = new ArrayList<>();
        
        // Crear 10 barcos por jugador (50 total)
        for (Jugador jugador : jugadores) {
            for (int i = 0; i < 10; i++) {
                Barco barco = new Barco();
                barco.setJugador(jugador);
                
                // Asignar modelo aleatoriamente
                Modelo modeloAleatorio = modelos.get(random.nextInt(modelos.size()));
                barco.setModelo(modeloAleatorio);
                
                // Posición inicial aleatoria en la primera fila (zona de partida)
                barco.setPosX(random.nextInt(10)); // 0-9
                barco.setPosY(0); // Primera fila
                
                // Velocidad aleatoria entre 1 y 3 para X e Y
                barco.setVelX(random.nextInt(3) + 1);
                barco.setVelY(random.nextInt(3) + 1);
                
                barcos.add(barco);
            }
        }
        
        barcoRepository.saveAll(barcos);
    }
}