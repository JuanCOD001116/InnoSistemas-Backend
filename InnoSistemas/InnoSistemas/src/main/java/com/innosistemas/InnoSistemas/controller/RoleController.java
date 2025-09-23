package com.innosistemas.InnoSistemas.controller;

import com.innosistemas.InnoSistemas.domain.Role;
import com.innosistemas.InnoSistemas.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Operation(summary = "Listar roles")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = Role.class)))
    @GetMapping
    public List<Role> list() { return roleRepository.findAll(); }

    @Operation(summary = "Crear rol")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado",
                    content = @Content(schema = @Schema(implementation = Role.class)))
    })
    @PostMapping
    public ResponseEntity<Role> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Role.class),
                            examples = @ExampleObject(value = "{\n  \"name\": \"ROLE_STUDENT\"\n}"))
            )
            @RequestBody Role role) {
        Role saved = roleRepository.save(role);
        return ResponseEntity.created(URI.create("/api/roles/" + saved.getId())).body(saved);
    }

    @Operation(summary = "Actualizar rol por ID")
    @ApiResponse(responseCode = "200", description = "Actualizado",
            content = @Content(schema = @Schema(implementation = Role.class)))
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable Integer id, @RequestBody Role role) {
        role.setId(id);
        return ResponseEntity.ok(roleRepository.save(role));
    }

    @Operation(summary = "Eliminar rol por ID")
    @ApiResponse(responseCode = "204", description = "Eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
