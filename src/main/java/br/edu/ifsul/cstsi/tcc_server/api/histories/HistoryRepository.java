package br.edu.ifsul.cstsi.tcc_server.api.histories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findTopByUserIdAndEpisodeIdOrderByIdDesc(Long userId, Long episodeId);
    void deleteByUserIdAndEpisodeId(Long userId, Long episodeId);
}
