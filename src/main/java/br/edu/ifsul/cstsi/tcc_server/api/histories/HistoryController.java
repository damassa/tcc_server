package br.edu.ifsul.cstsi.tcc_server.api.histories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Time;

@RestController
@RequestMapping("/api/v1/histories")
public class HistoryController { //TODO: Revisar com professor
    @Autowired
    private HistoryService service;

    @PostMapping
    public ResponseEntity<Time> insert (@RequestBody History history) {
        History h = service.insert(history);
        URI location = getUri(h.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<History> update(@RequestBody History history) {
        History h = service.update(history);
        return h != null ?
                ResponseEntity.ok(h) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Time> delete(@RequestBody HistoryKey hId) {
        return service.delete(hId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    private URI getUri(HistoryKey id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
