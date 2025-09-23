package com.innosistemas.InnoSistemas.controller;

import com.innosistemas.InnoSistemas.domain.Role;
import com.innosistemas.InnoSistemas.domain.User;
import com.innosistemas.InnoSistemas.repository.RoleRepository;
import com.innosistemas.InnoSistemas.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/roles")
public class UserRoleController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    record RoleName(String name) {}

    @GetMapping
    public ResponseEntity<List<String>> list(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(u -> ResponseEntity.ok(u.getRoles().stream().map(Role::getName).collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<List<String>> add(@PathVariable Long userId, @RequestBody RoleName body) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        Role role = roleRepository.findByName(body.name()).orElse(null);
        if (role == null) return ResponseEntity.badRequest().build();
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(roles.stream().map(Role::getName).collect(Collectors.toList()));
    }

    @DeleteMapping
    public ResponseEntity<List<String>> remove(@PathVariable Long userId, @RequestBody RoleName body) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        Role role = roleRepository.findByName(body.name()).orElse(null);
        if (role == null) return ResponseEntity.badRequest().build();
        Set<Role> roles = user.getRoles();
        roles.remove(role);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(roles.stream().map(Role::getName).collect(Collectors.toList()));
    }
}
