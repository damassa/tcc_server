package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findEpisodesBySerieId(Long id);
//    @Query(value = "SELECT e.name FROM episodes e WHERE e.name LIKE ?1", nativeQuery = true)
//    List<Episode> findByName(String name);
}
