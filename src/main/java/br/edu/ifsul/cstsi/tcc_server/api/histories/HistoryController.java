package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeRepository;
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
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/histories")
@CrossOrigin(origins = "http://localhost:3000")
public class HistoryController { // TODO: Rever sexta
    @Autowired
    private HistoryService service;

    @Autowired
    private UserRepository userRep;
    @Autowired
    private EpisodeRepository episodeRepository;

    @GetMapping("{id}")
    public ResponseEntity<HistoryDTOResponse> findById(@PathVariable("id") Long id) {
        var h = service.getHistoryById(id);
        System.out.println(h);
        return h != null ? ResponseEntity.ok(new HistoryDTOResponse(h)) : ResponseEntity.notFound().build();
    }


    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Time> insert (@RequestBody HistoryDTOResponse historyDTO) {
        History history = new History();
        history.setId(historyDTO.id());
        history.setUser(userRep.findById(historyDTO.idUser()).get());
        history.setEpisode(episodeRepository.findById(historyDTO.idEpisode()).get());
        history.setPausedAt(LocalTime.parse("16:42"));
        History h = service.insert(history);
        URI location = getUri(h.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<HistoryDTOResponse> update(@PathVariable("id") Long id, @RequestBody HistoryDTOPut historyDTOPut) {
        History history = new History();
        history.setId(id);
        history.setUser(userRep.findById(historyDTOPut.idUser()).get());
        history.setEpisode(episodeRepository.findById(historyDTOPut.idEpisode()).get());
        history.setPausedAt(LocalTime.parse("00:00"));

        return history != null ? ResponseEntity.ok(new HistoryDTOResponse(history)) : ResponseEntity.notFound().build();
//        History h = service.update(history);
//        return h != null ?
//                ResponseEntity.ok(h) :
//                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Time> delete(@PathVariable("id") Long id) {
        System.out.println(id);
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
