package co.edu.puj.regata.init;

import co.edu.puj.regata.domain.entity.*;
import co.edu.puj.regata.domain.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Component
public class DbInitializer {

    @Autowired
    private MapaRepo mapaRepo;
    @Autowired
    private CeldaRepo celdaRepo;
    @Autowired
    private JugadorRepo jugadorRepo;
    @Autowired
    private ModeloRepo modeloRepo;
    @Autowired
    private BarcoRepo barcoRepo;
    @Autowired
    private PartidaRepo partidaRepo;
    @Autowired
    private ParticipacionRepo participacionRepo;

    @PostConstruct
    public void init() {
        try {
        // Verifica si ya existen datos en la base de datos (lógica idempotente)
        if (mapaRepo.count() > 0) {
            System.out.println("Los datos ya existen en la base de datos. Saltando inicialización.");
            return;  // Si los datos ya existen, no hace nada
        }

        System.out.println("Iniciando poblado de base de datos...");

        // 1. Crear Mapa con celdas
        Mapa mapa = new Mapa();
        mapa.setNombre("Mapa Principal");
        mapa.setTamFilas(10);
        mapa.setTamColumnas(10);
        mapa = mapaRepo.save(mapa);

        // Crear celdas para el mapa
        List<Celda> celdas = new ArrayList<>();
        for (int x = 0; x < mapa.getTamFilas(); x++) {
            for (int y = 0; y < mapa.getTamColumnas(); y++) {
                Celda celda = new Celda();
                celda.setMapa(mapa);
                celda.setX(x);
                celda.setY(y);
                
                // Definir tipos de celda
                if (x == 0 || y == 0 || x == mapa.getTamFilas() - 1 || y == mapa.getTamColumnas() - 1) {
                    celda.setTipo(Celda.TipoCelda.PARED);  // Bordes del mapa como paredes
                } else if (x == 1 && y == 1) {
                    celda.setTipo(Celda.TipoCelda.PARTIDA);  // Punto de partida
                } else if (x == mapa.getTamFilas() - 2 && y == mapa.getTamColumnas() - 2) {
                    celda.setTipo(Celda.TipoCelda.META);  // Punto de meta
                } else {
                    celda.setTipo(Celda.TipoCelda.AGUA);  // Resto de celdas de agua
                }
                celdas.add(celda);
            }
        }
        celdaRepo.saveAll(celdas);

        // 2. Crear Modelos de Barco
        List<Modelo> modelos = new ArrayList<>();
        
        Modelo modelo1 = new Modelo();
        modelo1.setNombre("Velero Clásico");
        modelo1.setColor("#FF0000");
        modelos.add(modelo1);
        
        Modelo modelo2 = new Modelo();
        modelo2.setNombre("Yate Deportivo");
        modelo2.setColor("#00FF00");
        modelos.add(modelo2);
        
        Modelo modelo3 = new Modelo();
        modelo3.setNombre("Catamarán");
        modelo3.setColor("#0000FF");
        modelos.add(modelo3);
        
        Modelo modelo4 = new Modelo();
        modelo4.setNombre("Lancha Rápida");
        modelo4.setColor("#FFFF00");
        modelos.add(modelo4);
        
        Modelo modelo5 = new Modelo();
        modelo5.setNombre("Barco Pesquero");
        modelo5.setColor("#FF00FF");
        modelos.add(modelo5);
        
        Modelo modelo6 = new Modelo();
        modelo6.setNombre("Crucero");
        modelo6.setColor("#00FFFF");
        modelos.add(modelo6);
        
        Modelo modelo7 = new Modelo();
        modelo7.setNombre("Velero de Competición");
        modelo7.setColor("#FFA500");
        modelos.add(modelo7);
        
        Modelo modelo8 = new Modelo();
        modelo8.setNombre("Barco de Vela");
        modelo8.setColor("#800080");
        modelos.add(modelo8);
        
        Modelo modelo9 = new Modelo();
        modelo9.setNombre("Embarcación Ligera");
        modelo9.setColor("#008000");
        modelos.add(modelo9);
        
        Modelo modelo10 = new Modelo();
        modelo10.setNombre("Barco de Recreo");
        modelo10.setColor("#000080");
        modelos.add(modelo10);
        modeloRepo.saveAll(modelos);

        // 3. Crear Jugadores
        List<Jugador> jugadores = new ArrayList<>();
        
        // Administrador
        Jugador admin = new Jugador();
        admin.setNombre("Capitán Rodrigo");
        admin.setContraseña("admin123");
        admin.setRol(Jugador.RolUsuario.ADMIN);
        jugadores.add(admin);
        
        Jugador jugador1 = new Jugador();
        jugador1.setNombre("Marina Velasco");
        jugador1.setContraseña("player123");
        jugador1.setRol(Jugador.RolUsuario.JUGADOR);
        jugadores.add(jugador1);
        
        Jugador jugador2 = new Jugador();
        jugador2.setNombre("Almirante Torres");
        jugador2.setContraseña("password123");
        jugador2.setRol(Jugador.RolUsuario.JUGADOR);
        jugadores.add(jugador2);
        
        Jugador jugador3 = new Jugador();
        jugador3.setNombre("Navegante Silva");
        jugador3.setContraseña("password123");
        jugador3.setRol(Jugador.RolUsuario.JUGADOR);
        jugadores.add(jugador3);
        
        Jugador jugador4 = new Jugador();
        jugador4.setNombre("Timonel García");
        jugador4.setContraseña("password123");
        jugador4.setRol(Jugador.RolUsuario.JUGADOR);
        jugadores.add(jugador4);
        jugadorRepo.saveAll(jugadores);

        // 4. Crear Barcos (solo para jugadores, no para administrador)
        List<Barco> barcos = new ArrayList<>();
        // Crear barcos solo para jugadores (índices 1-4, saltando el administrador en índice 0)
        for (int i = 1; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            if (jugador.getRol() == Jugador.RolUsuario.JUGADOR) {
                // Crear 2 barcos por jugador
                for (int j = 0; j < 2; j++) {
                    Modelo modelo = modelos.get((i * 2 + j) % modelos.size());
                    
                    Barco barco = new Barco();
                    barco.setModelo(modelo);
                    barco.setJugador(jugador);
                    // Inicializar velocidades en 0 según el enunciado
                    barco.setVelocidadX(0);
                    barco.setVelocidadY(0);
                    barco.setEstado(EstadoBarco.VIVO);
                    barcos.add(barco);
                }
            }
        }
        barcoRepo.saveAll(barcos);

        // 5. Crear Partida
        Partida partida = new Partida();
        partida.setNombre("Regata de Prueba");
        partida.setMapa(mapa);
        partida.setFechaInicio(LocalDateTime.now());
        partida.setEstado(Partida.EstadoPartida.EN_CURSO); // Estado listo para simulación
        partida.setFechaFin(null);
        partida.setDuracionMaxima(30); // 30 minutos máximo
        partida.setMaxParticipantes(10);
        partida = partidaRepo.save(partida);

        // 6. Crear Participaciones para cada barco en la partida
        List<Participacion> participaciones = new ArrayList<>();
        for (int i = 0; i < barcos.size(); i++) {
            Barco barco = barcos.get(i);
            Participacion participacion = new Participacion();
            participacion.setPartida(partida);
            participacion.setBarco(barco);
            participacion.setJugador(barco.getJugador());
            // Distribuir barcos en línea de salida
            participacion.setPosX(50);  // Línea de salida
            participacion.setPosY(100 + (i * 50)); // Espaciados verticalmente
            participacion.setEliminado(false);
            participaciones.add(participacion);
        }
        participacionRepo.saveAll(participaciones);

        // Imprimir logs para verificar el conteo de datos creados
        System.out.println("=== Datos de inicialización completados ===");
        System.out.println("Mapas creados: " + mapaRepo.count());
        System.out.println("Celdas creadas: " + celdaRepo.count());
        System.out.println("Jugadores creados: " + jugadorRepo.count());
        System.out.println("Modelos creados: " + modeloRepo.count());
        System.out.println("Barcos creados: " + barcoRepo.count());
        System.out.println("Partidas creadas: " + partidaRepo.count());
        System.out.println("Participaciones creadas: " + participacionRepo.count());
        System.out.println("=== Inicialización completada exitosamente ===");
        } catch (Exception e) {
            System.err.println("Error durante la inicialización de datos de prueba: " + e.getMessage());
            e.printStackTrace();
            // No relanzar la excepción para evitar que falle el startup de la aplicación
        }
    }
}