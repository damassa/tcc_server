package br.edu.ifsul.cstsi.tcc_server.api.users;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String name,
    @NotBlank
    String email,
    @NotBlank
    String senha) {
}
