package br.edu.ifsul.cstsi.tcc_server.api.ratings;


public record RatingDTOResponse(
        Long id,
        Long idUser,
        Long idSerie,
        Integer stars,
        String comment,
        String userName
) {
    public RatingDTOResponse (Rating rating) {
        this(
                rating.getId(),
                rating.getUser() != null ? rating.getUser().getId() : null,
                rating.getSerie() != null ? rating.getSerie().getId() : null,
                rating.getStars(),
                rating.getComment(),
                rating.getUser() != null ? rating.getUser().getUsername() : null
        );
    }
}
