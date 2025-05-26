package br.edu.ifsul.cstsi.tcc_server.api.users;

import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        boolean emailConfirmed,
        List<String> roles
) {
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isConfirmed(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
        );

    }
}