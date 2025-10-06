package br.edu.ifsul.cstsi.tcc_server.api.ratings;


import jakarta.validation.constraints.NotNull;

public record RatingDTOPost(
        @NotNull
        Long idUser,
        @NotNull
        Long idSerie,
        @NotNull(message = "Número de estrelas é obrigatório")
        Integer stars
) {
    public RatingDTOPost (Rating rating) {
        this(rating.getUser().getId(), rating.getSerie().getId(), rating.getStars());
    }
}
