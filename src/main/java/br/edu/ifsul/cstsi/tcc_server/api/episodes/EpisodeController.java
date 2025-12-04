package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/episodes")
@CrossOrigin(origins = {"https://worldoftokusatsu.netlify.app/", "https://admin-toku.netlify.app/"})
public class EpisodeController {
    private final EpisodeService service;

    public EpisodeController(EpisodeService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<EpisodeDTOGet> getEpisodeById(@PathVariable Long id) {
        var ep = service.getEpisodeById(id);
        return ep != null ? ResponseEntity.ok(ep) : ResponseEntity.notFound().build();
    }

    @GetMapping("/serie/{serieId}")
    public ResponseEntity<List<EpisodeDTOGet>> getEpisodesBySerieId(@PathVariable Long serieId) {
        var episodes = service.getEpisodesBySerieId(serieId);
        return ResponseEntity.ok(episodes);
    }

    @PostMapping
    public ResponseEntity<EpisodeDTOGet> insert(@RequestBody EpisodeDTOPost dto, UriComponentsBuilder uriBuilder) {
        EpisodeDTOGet saved = service.insert(dto); // agora usa o método correto do service
        URI location = uriBuilder.path("/api/v1/episodes/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(location).body(saved);
    }



    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<EpisodeDTOGet> update(
            @PathVariable Long id,
            @Valid @RequestBody EpisodeDTOPut dto) {

        var e = service.update(dto, id);
        return e != null ? ResponseEntity.ok(e) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}

