// DTO DE ENTRADA (PUT) ---------------------------
package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EpisodeDTOPut(
        @NotBlank(message = "Nome não pode ser vazio")
        String name,
        @NotNull(message = "Duração não pode ser nula")
        int duration,
        @NotBlank(message = "Link não pode ser vazio")
        String link,
        @NotNull(message = "O episódio precisa estar associado a uma série")
        Long serieId
) { }
