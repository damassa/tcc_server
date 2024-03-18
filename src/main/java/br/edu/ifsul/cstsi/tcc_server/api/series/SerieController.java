package br.edu.ifsul.cstsi.tcc_server.api.series;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/series")
public class SerieController {
    @Autowired
    private SerieService service;

    @GetMapping
    public ResponseEntity<List<Serie>> selectAll() {
        List<Serie> series = service.getSeries();
        return ResponseEntity.ok(series);
    }

    @GetMapping("{id}")
    public ResponseEntity<Serie> selectById(@PathVariable("id") Long id) {
        Optional<Serie> s = service.getSerieById(id);
        if(s.isPresent()) {
            return ResponseEntity.ok(s.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Serie>> selectByName(@PathVariable("name") String name) {
        List<Serie> series = service.getSeriesByName(name);
        return series.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(series);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> insert(@RequestBody Serie serie) {
        Serie s = service.insert(serie);
        URI location = getUri(s.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Serie> update(@PathVariable("id") Long id, @Valid @RequestBody Serie serie){
        serie.setId(id);
        Serie s = service.update(serie, id);
        return s != null ?
                ResponseEntity.ok(s) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        return service.delete(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    //utilitário
    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
