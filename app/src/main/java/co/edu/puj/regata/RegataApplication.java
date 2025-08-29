package co.edu.puj.regata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Regata Online.
 * Esta clase arranca la aplicación Spring Boot.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0.0
 */
@SpringBootApplication
public class RegataApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(RegataApplication.class, args);
    }

}