package br.edu.ifsul.cstsi.tcc_server.api.series;

public record SerieDTOResponse(
        Long id,
        String name,
        String plot,
        Integer year,
        String image,
        String bigImage,
        String opening_video
) {
    public SerieDTOResponse(Serie serie) {
            this(serie.getId(), serie.getName(), serie.getPlot(), serie.getYear(), serie.getImage(), serie.getBigImage(), serie.getOpening_video());
    }
}
