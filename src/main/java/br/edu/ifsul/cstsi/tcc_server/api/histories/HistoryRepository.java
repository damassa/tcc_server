package br.edu.ifsul.cstsi.tcc_server.api.histories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> { //TODO: Revisar com professor
    Optional<History> findHistoryById(HistoryKey id);
}
