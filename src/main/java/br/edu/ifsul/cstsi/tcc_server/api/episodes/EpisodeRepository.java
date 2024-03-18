package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Optional<Episode> findById();
//    @Query(value = "SELECT e FROM Episode e where e.name like ?1")
}
