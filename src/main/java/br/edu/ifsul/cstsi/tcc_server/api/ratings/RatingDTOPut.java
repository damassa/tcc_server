package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import jakarta.validation.constraints.NotBlank;

public record RatingDTOPut(
        @NotBlank
        Long id,
        @NotBlank
        Long idUser,
        @NotBlank
        Long idSerie,
        @NotBlank
        String comment,
        @NotBlank
        Integer stars
) {
    public RatingDTOPut(Rating rating) {
        this(rating.getId(), rating.getUser().getId(), rating.getSerie().getId(), rating.getComment(), rating.getStars());
    }
}
