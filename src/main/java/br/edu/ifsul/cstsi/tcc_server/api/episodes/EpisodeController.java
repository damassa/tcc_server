package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/episodes")
public class EpisodeController {
    @Autowired
    private EpisodeService service;

    @GetMapping("{id}")
    public ResponseEntity<Episode> getEpisodeById(@PathVariable("id") Long id) {
        var ep = service.getEpisodeById(id);
        return ep != null ? ResponseEntity.ok().body(ep) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> insert (@RequestBody Episode episode) {
        Episode e = service.insert(episode);
        URI location = getUri(e.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Episode> update(@PathVariable("id") Long id, @Valid @RequestBody Episode episode) {
        episode.setId(id);
        Episode e = service.update(episode, id);
        return e != null ?
                ResponseEntity.ok(e) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return service.delete(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
