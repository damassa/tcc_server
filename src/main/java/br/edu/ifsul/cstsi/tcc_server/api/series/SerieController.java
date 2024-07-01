package br.edu.ifsul.cstsi.tcc_server.api.series;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.print.Pageable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/series")
public class SerieController {
    @Autowired
    private SerieService service;

    //TODO: Rever sexta
//    @GetMapping
//    public ResponseEntity<Page<SerieDTOResponse>> selectAll(@PageableDefault(size = 10, sort = "name") Pageable pagination) {
//        return ResponseEntity.ok(service.getSeries(pagination).map(SerieDTOResponse::new));
//    }

    //TODO: Rever sexta
//    @GetMapping("{id}")
//    public ResponseEntity<SerieDTOResponse> selectById(@PathVariable("id") Long id) {
//        var s = service.getSerieById(id);
//        if(s.isPresent()) {
//            return ResponseEntity.ok(new SerieDTOResponse(s.get()));
//        }
//        return ResponseEntity.notFound().build();
//    }

    //TODO: Rever sexta
//    @GetMapping("/name/{name}")
//    public ResponseEntity<List<SerieDTOResponse>> selectByName(@PathVariable("name") String name) {
//        var series = service.getSeriesByName(name);
//        return series.isEmpty() ?
//        ResponseEntity.noContent().build() :
//        ResponseEntity.ok(series.stream().map(SerieDTOResponse::new).collect(Collectors.toList()));
//    }

    //TODO: Rever sexta
//    @PostMapping
//    @Secured({"ROLE_ADMIN"})
//    public ResponseEntity<URI> insert(@RequestBody SerieDTOPost serieDTOPost, UriComponentsBuilder uriBuilder) {
//        var s = service.insert(new Serie(
//                null,
//                serieDTOPost.name(),
//                serieDTOPost.plot(),
//                serieDTOPost.year(),
//                serieDTOPost.image(),
//                serieDTOPost.bigImage(),
//                serieDTOPost.opening_video()
//        ));
//        var location = uriBuilder.path("api/v1/series/{id}").buildAndExpand(s.getId()).toUri();
//        return ResponseEntity.created(location).build();
//    }

    //TODO: Rever sexta
//    @PutMapping("{id}")
//    public ResponseEntity<SerieDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SerieDTOPut serieDTOPut) {
//        var s = service.update(new Serie(
//                id,
//                serieDTOPut.name(),
//                serieDTOPut.plot(),
//                serieDTOPut.year(),
//                serieDTOPut.image(),
//                serieDTOPut.bigImage(),
//                serieDTOPut.opening_video(),
//        ));
//        return s != null ?
//                ResponseEntity.ok(new SerieDTOResponse(s)) :
//                ResponseEntity.notFound().build();
//    }

    //TODO: Rever sexta
//    @DeleteMapping("{id}")
//    public ResponseEntity delete(@PathVariable("id") Long id) {
//        return service.delete(id) ?
//                ResponseEntity.ok().build() :
//                ResponseEntity.notFound().build();
//    }

    //TODO: Rever sexta
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }

    @GetMapping
    public ResponseEntity<List<Serie>> selectAll() {
        List<Serie> series = service.getSeries();
        System.out.println(series);
        return ResponseEntity.ok(series);
    }

    @GetMapping("{id}")
    public ResponseEntity<Serie> selectById(@PathVariable("id") Long id) {
        Optional<Serie> s = service.getSerieById(id);
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
