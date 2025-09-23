package com.innosistemas.InnoSistemas.controller;

import com.innosistemas.InnoSistemas.domain.Permission;
import com.innosistemas.InnoSistemas.domain.Role;
import com.innosistemas.InnoSistemas.domain.User;
import com.innosistemas.InnoSistemas.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Schema(name = "UserResponse")
    public record UserResponse(Long id, String username, String email) {}

    @Schema(name = "PermissionDTO")
    public record PermissionDTO(Integer id, String name) {}

    @Schema(name = "RoleDTO")
    public record RoleDTO(Integer id, String name, List<PermissionDTO> permissions) {}

    @Schema(name = "UserWithRolesDTO")
    public record UserWithRolesDTO(Long id, String username, String email, List<RoleDTO> roles) {}

    private static RoleDTO mapRole(Role role) {
        List<PermissionDTO> perms = role.getPermissions() == null ? List.of() : role.getPermissions().stream()
                .map(p -> new PermissionDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
        return new RoleDTO(role.getId(), role.getName(), perms);
    }

    private static UserWithRolesDTO mapUserWithRoles(User u) {
        List<RoleDTO> roleDTOs = u.getRoles() == null ? List.of() : u.getRoles().stream().map(UserController::mapRole).collect(Collectors.toList());
        return new UserWithRolesDTO(u.getId(), u.getUsername(), u.getEmail(), roleDTOs);
    }

    @Schema(name = "CreateUserRequest")
    public record CreateUserRequest(
            @Schema(example = "alumno1") String username,
            @Schema(example = "a1@uni.edu") String email,
            @Schema(example = "Secreta123") String password
    ) {}

    @Operation(summary = "Listar usuarios (básico)")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @GetMapping
    public List<UserResponse> list() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Listar usuarios con roles y permisos")
    @GetMapping("/with-roles")
    public List<UserWithRolesDTO> listWithRoles() {
        return userRepository.findAll().stream().map(UserController::mapUserWithRoles).collect(Collectors.toList());
    }

    @Operation(summary = "Obtener usuario por ID con roles y permisos")
    @GetMapping("/{id}/with-roles")
    public ResponseEntity<UserWithRolesDTO> getByIdWithRoles(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(UserController::mapUserWithRoles)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserRequest.class),
                            examples = @ExampleObject(value = "{\n  \"username\": \"alumno1\",\n  \"email\": \"a1@uni.edu\",\n  \"password\": \"Secreta123\"\n}"))
            )
            @RequestBody CreateUserRequest req) {
        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        User saved = userRepository.save(user);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId()))
                .body(new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail()));
    }

    @Operation(summary = "Actualizar usuario por ID")
    @ApiResponse(responseCode = "200", description = "Actualizado",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserRequest.class),
                            examples = @ExampleObject(value = "{\n  \"username\": \"alumno1\",\n  \"email\": \"a1@uni.edu\",\n  \"password\": \"NuevaClave123\"\n}"))
            )
            @RequestBody CreateUserRequest req) {
        User user = userRepository.findById(id).orElseGet(User::new);
        user.setId(id);
        user.setUsername(req.username());
        user.setEmail(req.email());
        if (req.password() != null && !req.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(req.password()));
        }
        User saved = userRepository.save(user);
        return ResponseEntity.ok(new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail()));
    }

    @Operation(summary = "Eliminar usuario por ID")
    @ApiResponse(responseCode = "204", description = "Eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
