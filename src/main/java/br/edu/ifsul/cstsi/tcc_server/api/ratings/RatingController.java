package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController { // TODO: Rever sexta
    @Autowired
    private RatingService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("{id}")
    public ResponseEntity<RatingDTOResponse> findById(@PathVariable("id") Long id) {
        var r = service.getRatingById(id);
        System.out.println(r);
        return r != null ? ResponseEntity.ok(new RatingDTOResponse(r)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> insert (@RequestBody RatingDTOResponse ratingDTO) {
        Rating rating = new Rating();
        rating.setId(ratingDTO.id());
        rating.setUser(userRepository.findById(ratingDTO.idUser()).get());
        rating.setSerie(serieRepository.findById(ratingDTO.idSerie()).get());
        rating.setComment(ratingDTO.comment());
        rating.setStars(ratingDTO.stars());
        Rating r = service.insert(rating);
        URI location = getUri(r.getId());

        return ResponseEntity.created(location).build();
//        Rating r = service.insert(rating);
//        URI location = getUri(r.getId());
//        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<RatingDTOResponse> update(@PathVariable("id") Long id, @RequestBody RatingDTOPut ratingDTOPut) {
        Rating rating = new Rating();
        rating.setId(id);
        rating.setUser(userRepository.findById(ratingDTOPut.idUser()).get());
        rating.setSerie(serieRepository.findById(ratingDTOPut.idSerie()).get());
        rating.setStars(ratingDTOPut.stars());
        rating.setComment(ratingDTOPut.comment());

        return rating != null ? ResponseEntity.ok(new RatingDTOResponse(rating)) : ResponseEntity.notFound().build();
//        Rating r = service.update(rating);
//        return r != null ?
//                ResponseEntity.ok(r) :
//                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return service.delete(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
