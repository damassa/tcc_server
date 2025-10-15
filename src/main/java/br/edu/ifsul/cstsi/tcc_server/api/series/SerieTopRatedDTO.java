package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.api.categories.Category;
import lombok.Getter;

public record SerieTopRatedDTO(
        Long id,
        String name,
        String plot,
        int year,
        String image,
        String bigImage,
        String opening_video,
        Long categoryId,
        String categoryName,
        Double averageStars,
        Long totalVotes
) {

    public SerieTopRatedDTO(Serie serie, Double averageStars, Long totalVotes) {
        this(
                serie.getId(),
                serie.getName(),
                serie.getPlot(),
                serie.getYear(),
                serie.getImage(),
                serie.getBigImage(),
                serie.getOpening_video(),


                serie.getCategory() != null ? serie.getCategory().getId() : null,
                serie.getCategory() != null ? serie.getCategory().getName() : null,
                averageStars,
                totalVotes
        );
    }
}
