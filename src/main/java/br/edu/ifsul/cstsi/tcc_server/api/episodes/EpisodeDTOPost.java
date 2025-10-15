// DTO DE ENTRADA (POST) ---------------------------
package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EpisodeDTOPost(
        @NotBlank(message = "Nome do episódio não pode ser vazio")
        String name,
        @NotNull(message = "Duração não pode ser nula")
        int duration,
        @NotBlank(message = "Link não pode ser vazio")
        String link,
        @NotNull(message = "O episódio precisa ser associado a uma série")
        Long serieId
) { }
