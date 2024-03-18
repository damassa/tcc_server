package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
//    @Query(value = "SELECT r FROM Rating r where r.comment like ?1")
//    List<Rating> findByComment(String comment);
}
