package br.edu.ifsul.cstsi.tcc_server.api.categories;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> selectAll() {
        List<Category> categories = service.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> selectById(@PathVariable("id") Long id) {
        Optional<Category> c = service.getCategoryById(id);
        return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Category>> selectByName(@PathVariable("name") String name) {
        System.out.println(name);
        List<Category> categories = service.getCategoriesByName(name);
        System.out.println(categories);
        return categories.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(categories);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> insert(@RequestBody Category category) {
        Category c = service.insert(category);
        URI location = getUri(c.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Category> update(@PathVariable("id") Long id, @Valid @RequestBody Category category) {
        category.setId(id);
        Category c = service.update(category, id);
        return c != null ?
                ResponseEntity.ok(c) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return service.delete(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }
}
