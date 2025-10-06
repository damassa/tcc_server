package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Rating insertOrUpdate(RatingDTOPost dto) {
        validateStars(dto.stars());

        var user = userRepository.findById(dto.idUser())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        var serie = serieRepository.findById(dto.idSerie())
                .orElseThrow(() -> new RuntimeException("Série não encontrada"));

        // Verifica se já existe voto desse usuário nessa série
        Optional<Rating> existing = rep.findBySerieIdAndUserId(dto.idSerie(), dto.idUser());

        Rating rating;
        if (existing.isPresent()) {
            rating = existing.get();
            rating.setStars(dto.stars()); // só atualiza as estrelas
        } else {
            rating = new Rating();
            rating.setUser(user);
            rating.setSerie(serie);
            rating.setStars(dto.stars());
        }

        return rep.save(rating);
    }

    public RatingStatsDTO getRatingStatsBySerieId(Long serieId) {
        RatingStatsDTO stats = rep.getRatingStatsBySerie(serieId);
        if(stats.getAverage() == null) {
            stats.setAverage(0.0);
            stats.setTotalVotes(0L);
        }

        return stats;
    }


    public Rating insert(RatingDTOPost dto) {
        validateStars(dto.stars());

        Rating rating = new Rating();
        rating.setUser(userRepository.findById(dto.idUser())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        rating.setSerie(serieRepository.findById(dto.idSerie())
                .orElseThrow(() -> new RuntimeException("Série não encontrada")));
        rating.setStars(dto.stars());

        return rep.save(rating);
    }

    public Rating update(RatingDTOPut dto) {
        validateStars(dto.stars());

        Rating rating = rep.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        rating.setUser(userRepository.findById(dto.idUser())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        rating.setSerie(serieRepository.findById(dto.idSerie())
                .orElseThrow(() -> new RuntimeException("Série não encontrada")));
        rating.setStars(dto.stars());

        return rep.save(rating);
    }

    public boolean delete(Long id) {
        if (rep.existsById(id)) {
            rep.deleteById(id);
            return true;
        }
        return false;
    }

    public double getAverageStarsBySerieId(Long serieId) {
        Double avg = rep.getAverageStarsBySerieId(serieId);
        return avg != null ? avg : 0.0;
    }

    public RatingStatsDTO getRatingStats(Long serieId) {
        return rep.getRatingStatsBySerie(serieId);
    }

    private void validateStars(Integer stars) {
        if (stars == null || stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Avaliação precisa ter entre 1 e 5 estrelas.");
        }
    }
}
