package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository rep;

    public Episode getEpisodeById(Long id){
        return rep.findById(id).orElse(null);
    }

    public List<Episode> getEpisodesBySerieId(Long id) {
        return rep.findEpisodesBySerieId(id);
    }

    public Episode insert(Episode episode) {
        Assert.isNull(episode.getId(), "Não foi possível inserir o registro.");
        return rep.save(episode);
    }

    public Episode update(Episode episode, Long id) {
        Assert.notNull(id, "Não foi possível atualizar o registro.");
        Optional<Episode> optional = rep.findById(id);
        if(optional.isPresent()) {
            Episode db = optional.get();

            db.setName(episode.getName());
            db.setDuration(episode.getDuration());
            return rep.save(db);
        } else {
            return null;
        }
    }

    public boolean delete(Long id) {
        Optional<Episode> optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        } else return false;
    }
}
