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

@RestController
@RequestMapping("api/v1/series")
@Api(value = "Séries")
public class SerieController {
    @Autowired
    private SerieService service;

    @GetMapping
    @ApiOperation(value = "Retorna todas as séries.")
    public ResponseEntity<List<SerieDTO>> selectAll() {
        List<SerieDTO> series = service.getSeries();
        return ResponseEntity.ok(series);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Retorna uma série pelo campo identificador.")
    public ResponseEntity<SerieDTO> selectById(@PathVariable("id") Long id) {
        SerieDTO s = service.getSerieById(id);
        return s != null ?
                ResponseEntity.ok(s) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    @ApiOperation(value = "Retorna uma lista de séries pelo nome.")
    public ResponseEntity<List<SerieDTO>> selectByName(@PathVariable("name") String name) {
        List<SerieDTO> series = service.getSeriesByName(name);
        return series.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(series);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "Adiciona uma série.")
    public ResponseEntity<String> insert(@RequestBody Serie serie) {
        SerieDTO s = service.insert(serie);
        URI location = getUri(s.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Altera os dados de uma série.")
    public ResponseEntity<SerieDTO> update(@PathVariable("id") Long id, @Valid @RequestBody Serie serie){
        serie.setId(id);
        SerieDTO p = service.update(serie, id);
        return p != null ?
                ResponseEntity.ok(p) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta uma série.")
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
