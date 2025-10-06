package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT avg (r.stars) from Rating r where r.serie.id = :serieId")
    Double getAverageStarsBySerieId(@Param("serieId") Long serieId);

    List<Rating> findBySerieId(Long serieId);

    Optional<Rating> findBySerieIdAndUserId(Long serieId, Long userId);

    @Query("SELECT new br.edu.ifsul.cstsi.tcc_server.api.ratings.RatingStatsDTO(AVG(r.stars), COUNT(r)) " +
            "FROM Rating r WHERE r.serie.id = :serieId")
    RatingStatsDTO getRatingStatsBySerie(@Param("serieId") Long serieId);
}
