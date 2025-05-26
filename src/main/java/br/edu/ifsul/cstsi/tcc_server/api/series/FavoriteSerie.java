package br.edu.ifsul.cstsi.tcc_server.api.series;

import jakarta.validation.constraints.NotBlank;

public record FavoriteSerie(
        @NotBlank
        String big_image,
        @NotBlank
        String image,
        @NotBlank
        String name,
        @NotBlank
        String opening_video,
        @NotBlank
        String plot,
        @NotBlank
        String year
) {
}
