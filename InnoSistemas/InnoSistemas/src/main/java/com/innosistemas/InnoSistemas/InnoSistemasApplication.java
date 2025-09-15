package com.innosistemas.InnoSistemas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class InnoSistemasApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnoSistemasApplication.class, args);
	}

	@Bean
	CommandLineRunner verifyDatabaseConnection(DataSource dataSource) {
		return args -> {
			try (Connection connection = dataSource.getConnection()) {
				System.out.println("[DB] Conexión exitosa a la base de datos: " + connection.getMetaData().getURL());
			} catch (Exception ex) {
				System.err.println("[DB] Error conectando a la base de datos: " + ex.getMessage());
			}
		};
	}
}
