package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoryService {
    private final HistoryRepository rep;
    private final UserRepository userRep;
    private final EpisodeRepository episodeRep;

    public HistoryService(HistoryRepository rep, UserRepository userRep, EpisodeRepository episodeRep) {
        this.rep = rep;
        this.userRep = userRep;
        this.episodeRep = episodeRep;
    }

    public Optional<History> getHistoryByUserAndEpisode(Long userId, Long episodeId) {
        return rep.findTopByUserIdAndEpisodeIdOrderByIdDesc(userId, episodeId);
    }

    @Transactional
    public History saveOrUpdate(Long userId, Long episodeId, Long pausedAt) {
        var user = userRep.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        var episode = episodeRep.findById(episodeId)
                .orElseThrow(() -> new IllegalArgumentException("Episódio não encontrado"));

        // 🔹 Se o usuário assistiu até o fim (>= duração), limpamos o histórico
        if (pausedAt != null && pausedAt >= episode.getDuration()) {
            pausedAt = 0L;
        }

        History history = getHistoryByUserAndEpisode(userId, episodeId).orElseGet(() -> {
            History h = new History();
            h.setUser(user);
            h.setEpisode(episode);
            return h;
        });

        history.setPausedAt(pausedAt);
        return rep.save(history);
    }


    public History getHistoryById(Long id){
        return rep.findById(id).orElse(null);
    }

    public History insert (History history) {
        System.out.println("DEBUG DOS GURI: ");
        System.out.println(history);
       if (history.getId() != null) {
           throw new IllegalArgumentException("O id deve ser nulo pela inserção");
       }
        return rep.save(history);
    }

    public History update(History history) {
        if (history.getId() == null) {
            throw new IllegalArgumentException("O id é obrigatório para atualizar");
        }

        return rep.findById(history.getId()).map(db -> {
            db.setPausedAt(history.getPausedAt());
            return rep.save(db);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return rep.findById(id).map(h -> {
            rep.delete(h);
            return true;
        }).orElse(false);
    }

    @Transactional
    public void deleteByUserAndEpisode(Long userId, Long episodeId) {
        rep.deleteByUserIdAndEpisodeId(userId, episodeId);
    }
}
