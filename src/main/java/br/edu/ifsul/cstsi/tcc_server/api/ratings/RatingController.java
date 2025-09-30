package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    @Autowired
    private RatingService service;

    @GetMapping("{id}")
    public ResponseEntity<RatingDTOResponse> findById(@PathVariable("id") Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/serie/{serieId}")
    public ResponseEntity<List<RatingDTOResponse>> findBySerie(@PathVariable("serieId") Long serieId) {
        List<RatingDTOResponse> ratings = service.getBySerie(serieId);
        return ratings.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ratings);
    }

    // Se quiser permitir que qualquer usuário avalie, remova o @Secured
    @PostMapping
    public ResponseEntity<RatingDTOResponse> insert(@Valid @RequestBody RatingDTOPost dto) {
        Rating saved = service.insert(dto);
        URI location = getUri(saved.getId());
        return ResponseEntity.created(location).body(new RatingDTOResponse(saved));
    }

    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<RatingDTOResponse> update(@PathVariable("id") Long id,
                                                    @Valid @RequestBody RatingDTOPut dto) {
        if (!id.equals(dto.id())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Rating updated = service.update(dto);
        return ResponseEntity.ok(new RatingDTOResponse(updated));
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = service.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }


    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
