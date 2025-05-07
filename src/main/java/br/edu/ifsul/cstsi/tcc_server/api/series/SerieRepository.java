package br.edu.ifsul.cstsi.tcc_server.api.series;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    @Query(value = "SELECT s FROM Serie s where s.name like ?1")
    List<Serie> findByName(String name);

    @Query(value = "SELECT s FROM Serie s ORDER BY s.year DESC ")
    List<Serie> orderSerieByYear();

    @Query(
            value = "SELECT s.* FROM tcc_server.series s WHERE s.categories_id = ?1 ORDER BY s.year", nativeQuery = true
    )
    List<Serie> getSeriesByCategoryId(Long id);

    @Query(value = "insert into tcc_server.favorites (serie_id, user_id) values(?1, ?2)", nativeQuery = true)
    void insertFavorite(Long id_serie, Long id_user);

    @Query(value = "select s.* from favorites f inner join series s, users u where f.serie_id = s.id and f.user_id = u.id", nativeQuery = true)
    List<Serie> getFavoritesByUserID(Long id);

    @Query(value = "delete from tcc_server.favorites where serie_id = ?1 and user_id = ?2", nativeQuery = true)
    void removeFromFavorites(Long id_serie, Long id_user);

    @Query(value = "select count(*) from tcc_server.favorites where serie_id = ?1 and user_id = ?2", nativeQuery = true)
    Integer countSeriesByIds(Long id_serie, Long id_user);
}