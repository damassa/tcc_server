// DTO DE SAÍDA (GET) ---------------------------
package br.edu.ifsul.cstsi.tcc_server.api.episodes;

public record EpisodeDTOGet(
        Long id,
        String name,
        int duration,
        String link,
        Long serieId // retorna também o id da série associada
) {
    public EpisodeDTOGet(Episode episode) {
        this(
                episode.getId(),
                episode.getName(),
                episode.getDuration(),
                episode.getLink(),
                episode.getSerie() != null ? episode.getSerie().getId() : null
        );
    }
}
