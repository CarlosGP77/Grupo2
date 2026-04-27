package com.example.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class TiendaApplication {
    // SpringApplication.run() arranca el servidor Tomcat embebido,
    // inicializa el contexto de Spring, conecta con la base de datos
    // y deja la aplicación escuchando en http://localhost:8080
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}