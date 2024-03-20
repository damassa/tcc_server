package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {
    @Autowired
    private RatingService service;

    @PostMapping
    public ResponseEntity<String> insert (@RequestBody Rating rating) {
        Rating r = service.insert(rating);
        URI location = getUri(r.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Rating> update(@RequestBody Rating rating) {
        Rating r = service.update(rating);
        return r != null ?
                ResponseEntity.ok(r) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return service.delete(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    private URI getUri(RatingKey id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
