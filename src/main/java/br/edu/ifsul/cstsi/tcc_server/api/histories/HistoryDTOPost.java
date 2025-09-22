package br.edu.ifsul.cstsi.tcc_server.api.histories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record HistoryDTOPost(
        @NotNull
        Long id,
        @NotNull
        Long idUser,
        @NotNull
        Long idEpisode,
        @NotNull
        Long pausedAt
) {
    public HistoryDTOPost(History history) {
        this(history.getId(), history.getUser().getId(), history.getEpisode().getId(), history.getPausedAt());
    }
}
