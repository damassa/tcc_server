package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository rep;

    public Episode insert(Episode episode) {
        Assert.isNull(episode.getId(), "Não foi possível inserir o registro.");
        return rep.save(episode);
    }

    public Episode update(Episode episode, Long id) {
        Assert.notNull(id, "Não foi possível atualizar o registro.");
        Optional<Episode> optional = rep.findById();
        if(optional.isPresent()) {
            Episode db = optional.get();

            db.setName(episode.getName());
            System.out.println("Episódio id " + db.getId());
            return rep.save(db);
        } else {
            return null;
        }
    }

    public boolean delete(Long id) {
        Optional<Episode> optional = rep.findById();
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        } else return false;
    }
}
