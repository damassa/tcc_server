package br.edu.ifsul.cstsi.tcc_server.api.series;

import jakarta.validation.constraints.NotBlank;

public record FavoriteDTOPost(
        @NotBlank
        Long serie_id,
        @NotBlank
        Long user_id
) {

}
