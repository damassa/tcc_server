package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository rep;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SerieRepository serieRepository;

    public Optional<RatingDTOResponse> getById(Long id) {
        return rep.findById(id)
                .map(RatingDTOResponse::new);
    }

    public List<RatingDTOResponse> getBySerie(Long serieId) {
        return rep.findBySerieId(serieId).stream().map(RatingDTOResponse::new).toList();
    }

    public Rating insert(RatingDTOPost dto) {
        if ((dto.comment() == null || dto.comment().isBlank()) && dto.stars() == null) {
            throw new IllegalArgumentException("Avaliação precisa ter comentário ou estrelas.");
        }

        Rating rating = new Rating();
        rating.setUser(userRepository.findById(dto.idUser())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        rating.setSerie(serieRepository.findById(dto.idSerie())
                .orElseThrow(() -> new RuntimeException("Série não encontrada")));
        rating.setStars(dto.stars());
        rating.setComment(dto.comment());

        return rep.save(rating);
    }

    public Rating update(RatingDTOPut dto) {
        if ((dto.comment() == null || dto.comment().isBlank()) && dto.stars() == null) {
            throw new IllegalArgumentException("Avaliação precisa ter comentário ou estrelas.");
        }

        Rating rating = rep.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        rating.setUser(userRepository.findById(dto.idUser())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        rating.setSerie(serieRepository.findById(dto.idSerie())
                .orElseThrow(() -> new RuntimeException("Série não encontrada")));
        rating.setStars(dto.stars());
        rating.setComment(dto.comment());

        return rep.save(rating);
    }

    public boolean delete(Long id) {
        if (rep.existsById(id)) {
            rep.deleteById(id);
            return true;
        }
        return false;
    }
}
