package com.innosistemas.InnoSistemas;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/health")
public class DatabaseHealthController {
	private final DataSource dataSource;

	public DatabaseHealthController(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Operation(summary = "Salud DB", description = "Verifica conexión a la base de datos")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "DB UP",
					content = @Content(schema = @Schema(implementation = Object.class))),
			@ApiResponse(responseCode = "503", description = "DB DOWN")
	})
	@GetMapping("/db")
	public ResponseEntity<Map<String, Object>> databaseHealth() {
		Map<String, Object> body = new HashMap<>();
		try (Connection connection = dataSource.getConnection()) {
			body.put("status", "UP");
			body.put("url", connection.getMetaData().getURL());
			body.put("product", connection.getMetaData().getDatabaseProductName());
			return ResponseEntity.ok(body);
		} catch (Exception ex) {
			body.put("status", "DOWN");
			body.put("error", ex.getMessage());
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
		}
	}
}
