package br.edu.ifsul.cstsi.tcc_server.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioAuthDTO(
    @Email @NotBlank
    String email,
    @NotBlank
    String senha) {
}
