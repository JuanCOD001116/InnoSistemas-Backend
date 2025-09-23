package com.innosistemas.InnoSistemas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Operation(summary = "Obtener proyecto por código",
            description = "Devuelve info si el usuario tiene permiso; 403 si no",
            security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acceso permitido",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
    public ResponseEntity<?> getProject(@PathVariable String code, Authentication auth) {
        boolean isStudent = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
        boolean isTeacher = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

        boolean allowed = (isStudent && code.equalsIgnoreCase("A")) || (isTeacher && code.equalsIgnoreCase("B"));
        if (!allowed) {
            System.out.println("[AUDIT] Acceso denegado | user=" + auth.getName() + " | project=" + code + " | at=" + OffsetDateTime.now());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "message", "Acceso denegado"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "code", code,
                "owner", isTeacher ? "teacher" : "student",
                "status", "active"
        ));
    }
}
