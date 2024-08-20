package br.edu.ifsul.cstsi.tcc_server.api.histories;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record HistoryDTOPut(
        @NotBlank
        Long id,
        @NotBlank
        Long idUser,
        @NotBlank
        Long idEpisode,
        @NotBlank
        LocalTime pausedAt
) {
    public HistoryDTOPut(History history) {
        this(history.getId(), history.getUser().getId(), history.getEpisode().getId(), history.getPausedAt());
    }
}
