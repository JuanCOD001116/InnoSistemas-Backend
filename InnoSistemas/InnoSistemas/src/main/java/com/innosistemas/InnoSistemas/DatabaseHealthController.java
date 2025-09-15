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

@RestController
@RequestMapping("/api/health")
public class DatabaseHealthController {
	private final DataSource dataSource;

	public DatabaseHealthController(DataSource dataSource) {
		this.dataSource = dataSource;
	}

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
