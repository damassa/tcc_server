package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpisodeService {
    private final EpisodeRepository rep;
    private final SerieRepository serieRep;

    public EpisodeService (EpisodeRepository rep, SerieRepository serieRep) {
        this.rep = rep;
        this.serieRep = serieRep;
    }

    public List<EpisodeDTOGet> getAll() {
        return rep.findAll().stream().map(EpisodeDTOGet::new).toList();
    }

    public EpisodeDTOGet getById(Long id) {
        Episode episode = rep.findById(id).orElseThrow(() -> new RuntimeException("Episódio não encontrado"));
        return new EpisodeDTOGet(episode);
    }

    public EpisodeDTOGet getEpisodeById(Long id){
        return rep.findById(id).map(EpisodeDTOGet::new).orElse(null);
    }


    public List<EpisodeDTOGet> getEpisodesBySerieId(Long serieId) {
        return rep.findEpisodesBySerieId(serieId).stream()
                .map(EpisodeDTOGet::new)
                .toList();
    }

    public EpisodeDTOGet insert(EpisodeDTOPost dto) {
        Serie serie = serieRep.findById(dto.serieId()).orElseThrow(() -> new RuntimeException("Série não encontrada"));

        Episode episode = new Episode();
        episode.setName(dto.name());
        episode.setDuration(dto.duration());
        episode.setLink(dto.link());
        episode.setSerie(serie);

        Episode saved = rep.save(episode);
        return new EpisodeDTOGet(saved);
    }

    public EpisodeDTOGet update(EpisodeDTOPut dto, Long id) {
        Episode episode = rep.findById(id).orElseThrow(() -> new RuntimeException("Episódio não encontrado"));

        Serie serie = serieRep.findById(dto.serieId()).orElseThrow(() -> new RuntimeException("Série não encontrada"));

        episode.setName(dto.name());
        episode.setDuration(dto.duration());
        episode.setLink(dto.link());
        episode.setSerie(serie);

        Episode updated = rep.save(episode);
        return new EpisodeDTOGet(updated);
    }

    public boolean delete(Long id) {
        return rep.findById(id).map(ep -> {
           rep.delete(ep);
           return true;
        }).orElse(false);
    }
}

