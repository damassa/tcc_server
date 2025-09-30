package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import jakarta.validation.constraints.NotNull;

public record RatingDTOPut(
        @NotNull
        Long id,
        @NotNull
        Long idUser,
        @NotNull
        Long idSerie,
        String comment,
        Integer stars
) {
    public RatingDTOPut(Rating rating) {
        this(rating.getId(), rating.getUser().getId(), rating.getSerie().getId(), rating.getComment(), rating.getStars());
    }
}
