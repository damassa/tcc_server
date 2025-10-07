package br.edu.ifsul.cstsi.tcc_server.api.series;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    @Query(value = "SELECT s FROM Serie s where s.name like ?1")
    @EntityGraph(attributePaths = {"category","episodes"})
    List<Serie> findByName(String name);

//    @Query(value = "SELECT s FROM Serie s ORDER BY s.year DESC ")
//    List<Serie> orderSerieByYear();

    @Query(
            value = "SELECT s FROM Serie s WHERE s.category.id = ?1 ORDER BY s.year"

    )
    @EntityGraph(attributePaths = {"category","episodes"})
    List<Serie> getSeriesByCategoryId(Long id);

    @Query(value = "insert into tcc_server.favorites (serie_id, user_id) values(?1, ?2)", nativeQuery = true)
    void insertFavorite(Long id_serie, Long id_user);

    @Query(value = "SELECT DISTINCT s.*\n" +
            "FROM favorites f\n" +
            "INNER JOIN series s ON f.serie_id = s.id\n" +
            "WHERE f.user_id = ?1\n", nativeQuery = true)
    List<Serie> getFavoritesByUserID(Long id);

    @Query(value = "delete from tcc_server.favorites where serie_id = ?1 and user_id = ?2", nativeQuery = true)
    void removeFromFavorites(Long id_serie, Long id_user);

    @Query(value = "select count(*) from tcc_server.favorites where serie_id = ?1 and user_id = ?2", nativeQuery = true)
    Integer countSeriesByIds(Long id_serie, Long id_user);

    @EntityGraph(attributePaths = {"category","episodes"})
    Optional<Serie> findById(Long id);

    @EntityGraph(attributePaths = {"category", "episodes"})
    List<Serie> findAll();

    @Query("""
    SELECT s
    FROM Serie s
    LEFT JOIN s.ratings r
    GROUP BY s
    HAVING COALESCE(AVG(r.stars), 0) >= 10
    ORDER BY AVG(r.stars) DESC
""")
    @EntityGraph(attributePaths = {"category", "episodes"})
    Page<Serie> findTopRatedSeries(Pageable pageable);


}