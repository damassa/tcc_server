package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import jakarta.validation.constraints.NotBlank;

public record RatingDTOResponse(
        Long id,
        Long idUser,
        Long idSerie,
        Integer stars,
        String comment
) {
    public RatingDTOResponse (Rating rating) {
        this(rating.getId(), rating.getUser().getId(), rating.getSerie().getId(), rating.getStars(), rating.getComment());
    }
}
