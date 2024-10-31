package br.edu.ifsul.cstsi.tcc_server.api.series;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/series")
public class SerieController {
    @Autowired
    private SerieService service;

    //TODO: Rever sexta
    @GetMapping
    public ResponseEntity<Page<SerieDTOResponse>> selectAll(@PageableDefault(sort = "name") Pageable pagination) {
        return ResponseEntity.ok(service.getSeries(pagination).map(SerieDTOResponse::new));
    }

    //TODO: Rever sexta
    @GetMapping("{id}")
    public ResponseEntity<SerieDTOResponse> selectById(@PathVariable("id") Long id) {
        var s = service.getSerieById(id);
        return s.map(serie -> ResponseEntity.ok(new SerieDTOResponse(serie))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //TODO: Rever sexta
    @GetMapping("/name/{name}")
    public ResponseEntity<List<SerieDTOResponse>> selectByName(@PathVariable("name") String name) {
        var series = service.getSeriesByName(name);
        return series.isEmpty() ?
        ResponseEntity.noContent().build() :
        ResponseEntity.ok(series.stream().map(SerieDTOResponse::new).collect(Collectors.toList()));
    }

    //TODO: Rever sexta
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<URI> insert(@RequestBody SerieDTOPost serieDTOPost, UriComponentsBuilder uriBuilder) {
        var s = service.insert(new Serie(
                serieDTOPost.name(),
                serieDTOPost.plot(),
                serieDTOPost.year(),
                serieDTOPost.image(),
                serieDTOPost.bigImage(),
                serieDTOPost.opening_video()
        ));
        var location = uriBuilder.path("api/v1/series/{id}").buildAndExpand(s.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    //TODO: Rever sexta
    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<SerieDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SerieDTOPut serieDTOPut) {
        var s = service.update(new Serie(
                serieDTOPut.name(),
                serieDTOPut.plot(),
                serieDTOPut.year(),
                serieDTOPut.image(),
                serieDTOPut.bigImage(),
                serieDTOPut.opening_video()
        ), id);
        return s != null ?
                ResponseEntity.ok(new SerieDTOResponse(s)) :
                ResponseEntity.notFound().build();
    }

    //TODO: Rever sexta
    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity delete(@PathVariable("id") Long id) {
        return service.delete(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    //TODO: Rever sexta
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
