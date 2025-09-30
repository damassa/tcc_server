package br.edu.ifsul.cstsi.tcc_server.api.histories;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/histories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class HistoryController {

    private final HistoryService service;

    public HistoryController(HistoryService service) {
        this.service = service;
    }

    /** Busca o último histórico do usuário para o episódio */
    @GetMapping("/search")
    @Secured({"ROLE_USER"})
    public ResponseEntity<HistoryDTOResponse> findByUserAndEpisode(
            @RequestParam Long userId,
            @RequestParam Long episodeId
    ) {
        return service.getHistoryByUserAndEpisode(userId, episodeId)
                .map(h -> ResponseEntity.ok(new HistoryDTOResponse(h)))
                .orElse(ResponseEntity.noContent().build()); // retorna 200 com corpo vazio se não houver histórico
    }

    /** Busca histórico por id */
    @GetMapping("/{id}")
    public ResponseEntity<HistoryDTOResponse> findById(@PathVariable Long id) {
        var h = service.getHistoryById(id);
        return h != null ? ResponseEntity.ok(new HistoryDTOResponse(h)) : ResponseEntity.notFound().build();
    }

    /** Cria ou atualiza o histórico do episódio */
    @PostMapping
    @Secured({"ROLE_USER"})
    public ResponseEntity<HistoryDTOResponse> saveOrUpdate(@RequestBody HistoryDTOPost dto) {
        History history = service.saveOrUpdate(dto.idUser(), dto.idEpisode(), dto.pausedAt());
        return ResponseEntity.ok(new HistoryDTOResponse(history));
    }

    @DeleteMapping
    @Secured({"ROLE_USER"})
    public ResponseEntity<Void> delete(@RequestParam Long userId, @RequestParam Long episodeId) {
        service.deleteByUserAndEpisode(userId, episodeId);
        return ResponseEntity.noContent().build();
    }

    /** Gera URI para localização de recurso (opcional) */
    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
