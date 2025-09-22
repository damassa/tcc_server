package br.edu.ifsul.cstsi.tcc_server.api.series;


public record FavoriteSerieDTO(
        Long id,
        String name,
        String plot,
        int year,
        String image,
        String bigImage,
        String opening_video,
        Long categoryId
) {
    public FavoriteSerieDTO(Serie serie) {
        this(
                serie.getId(),
                serie.getName(),
                serie.getPlot(),
                serie.getYear(),
                serie.getImage(),
                serie.getBigImage(),
                serie.getOpening_video(),
                serie.getCategory() != null ? serie.getCategory().getId() : null
        );
    }
}
