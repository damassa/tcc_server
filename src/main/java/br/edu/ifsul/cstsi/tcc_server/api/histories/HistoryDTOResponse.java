package br.edu.ifsul.cstsi.tcc_server.api.histories;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record HistoryDTOResponse(
        Long id,
        Long idUser,
        Long idEpisode,
        LocalTime pausedAt
) {
        public HistoryDTOResponse (History history) {
               this(history.getId(), history.getUser().getId(), history.getEpisode().getId(), history.getPausedAt());
        }
}
