package br.edu.ifsul.cstsi.tcc_server.api.series;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/series")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class SerieController {
    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }


    @GetMapping
    public ResponseEntity<List<SerieDTOResponse>> selectAllSeries() {
        var series = serieService.getAllSeries(); // retorna List<SerieDTOResponse>
        if (series.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(series);
    }



    @GetMapping("/pageable")
    public ResponseEntity<Page<SerieDTOResponse>> selectAll(@PageableDefault(sort = "name") Pageable pagination) {
        return ResponseEntity.ok(serieService.getSeries(pagination));
    }

    @GetMapping("/top-rated/pageable")
    public ResponseEntity<Page<SerieTopRatedDTO>> getTopRatedSeriesPageable(Pageable pageable) {
        Page<SerieTopRatedDTO> page = serieService.getTopRatedSeriesPageable(pageable);
        return ResponseEntity.ok(page);
    }



    @GetMapping("/{id}")
    public ResponseEntity<SerieDTOResponse> selectById(@PathVariable Long id) {
        SerieDTOResponse serie = serieService.getSerieById(id);
        return ResponseEntity.ok(serie);
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<List<SerieDTOResponse>> selectSeriesByCategoryId(@PathVariable Long id) {
        var series = serieService.getSeriesByCategoryId(id);
        if (series.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(
                series.stream().map(SerieDTOResponse::new).collect(Collectors.toList())
        );
    }


    @GetMapping("/sorted")
    public ResponseEntity<List<SerieDTOResponse>> getSeriesSorted(@RequestParam boolean asc) {
        var series = serieService.getSeriesOrderedByYear(asc).stream().map(SerieDTOResponse::new).toList();
        return ResponseEntity.ok(series);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<SerieDTOResponse>> selectByName(@PathVariable String name) {
        var series = serieService.getSeriesByName(name);
        if (series.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(
                series.stream().map(SerieDTOResponse::new).collect(Collectors.toList())
        );
    }


    @PostMapping("/addToFavorites")
    public ResponseEntity addSerieToFavorites(@RequestBody FavoriteDTOPost favoriteDTOPost) {
        System.out.println(favoriteDTOPost);
        serieService.toggleFavorite(favoriteDTOPost.serie_id(), favoriteDTOPost.user_id());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/removeFromFavorites")
    public ResponseEntity removeFromFavorites(@RequestBody FavoriteDTOPost favoriteDTOPost) {
        System.out.println(favoriteDTOPost);
        serieService.removeFromFavorites(favoriteDTOPost.serie_id(), favoriteDTOPost.user_id());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorites/count")
    public ResponseEntity<Integer> countFavoritesBySerieId(@RequestBody FavoriteDTOPost favoriteDTOPost) {
        var count = serieService.getFavoritesBySerieId(favoriteDTOPost.serie_id(), favoriteDTOPost.user_id());
        return ResponseEntity.ok(count);
    }

    //TODO: Rever sexta
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<URI> insert(@RequestBody SerieDTOPost serieDTOPost, UriComponentsBuilder uriBuilder) {
        Serie s = serieService.insert(serieDTOPost);
//        Category category = categoryService.getCategoryById(serieDTOPost.categoryId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não encontrada"));
//
//        Serie s = new Serie(
//                serieDTOPost.name(),
//                serieDTOPost.plot(),
//                serieDTOPost.year(),
//                serieDTOPost.image(),
//                serieDTOPost.bigImage(),
//                serieDTOPost.opening_video()
//        );
//        s.setCategory(category);
//
//        serieService.insert(s);

        var location = uriBuilder.path("api/v1/series/{id}").buildAndExpand(s.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    //TODO: Rever sexta
    @PatchMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<SerieDTOResponse> update(@PathVariable("id") Long id, @Valid @RequestBody SerieDTOPut serieDTOPut) {
        SerieDTOResponse updated = serieService.update(id, serieDTOPut); // Já é SerieDTOResponse
        return ResponseEntity.ok(updated); // <-- sem criar outro DTO
    }



    //TODO: Rever sexta
    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity delete(@PathVariable("id") Long id) {
        return serieService.delete(id) ?
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
