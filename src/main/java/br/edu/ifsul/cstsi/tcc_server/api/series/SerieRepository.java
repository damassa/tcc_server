package br.edu.ifsul.cstsi.tcc_server.api.series;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    @Query(value = "SELECT s FROM Serie s where s.name like ?1")
    List<Serie> findByName(String name);
}
