package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeDTOGet;

import java.util.List;
import java.util.stream.Collectors;

public record SerieDTOResponse(
        Long id,
        String name,
        String plot,
        int year,
        String image,
        String bigImage,
        String opening_video,
        Long categoryId,
        String categoryName,
        List<EpisodeDTOGet> episodes,
        Double avgRating,
        Long totalRatings
) {
    public SerieDTOResponse(Serie serie) {
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
                serie.getEpisodes() != null
                        ? serie.getEpisodes().stream().map(EpisodeDTOGet::new).collect(Collectors.toList())
                        : List.of(),null,null
        );
    }

    public SerieDTOResponse(Serie serie, Double avgRating, Long totalRatings) {
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
                serie.getEpisodes() != null
                    ? serie.getEpisodes().stream().map(EpisodeDTOGet::new).collect(Collectors.toList())
                    : List.of(),
                avgRating,
                totalRatings
        );
    }
}
