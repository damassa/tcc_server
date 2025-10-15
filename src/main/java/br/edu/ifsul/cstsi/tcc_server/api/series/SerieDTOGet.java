package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeDTOGet;
import java.util.List;

public record SerieDTOGet(
        Long id,
        String name,
        int year,
        String plot,
        String image,
        String bigImage,
        String openingVideo,
        Long categoryId,
        List<EpisodeDTOGet> episodes
) {
    public SerieDTOGet(Serie serie) {
        this(
                serie.getId(),
                serie.getName(),
                serie.getYear(),
                serie.getPlot(),
                serie.getImage(),
                serie.getBigImage(),
                serie.getOpening_video(),
                serie.getCategory().getId(),
                serie.getEpisodes() != null
                        ? serie.getEpisodes().stream().map(EpisodeDTOGet::new).toList()
                        : List.of()
        );
    }
}
