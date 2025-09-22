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
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class CategoryController {

    @Autowired
    private CategoryService service;

    // Retorna todos os DTOs
    @GetMapping
    public ResponseEntity<List<CategoryDTOPost>> selectAll() {
        List<CategoryDTOPost> categories = service.getCategories()
                .stream()
                .map(CategoryDTOPost::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // Retorna DTO por ID
    @GetMapping("{id}")
    public ResponseEntity<CategoryDTOPost> selectById(@PathVariable("id") Long id) {
        Optional<Category> c = service.getCategoryById(id);
        return c.map(cat -> ResponseEntity.ok(new CategoryDTOPost(cat)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Retorna lista de DTOs filtrando por nome
    @GetMapping("/name/{name}")
    public ResponseEntity<List<CategoryDTOPost>> selectByName(@PathVariable("name") String name) {
        List<CategoryDTOPost> categories = service.getCategoriesByName(name)
                .stream()
                .map(CategoryDTOPost::new)
                .collect(Collectors.toList());
        return categories.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(categories);
    }

    // Inserção via DTO
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<CategoryDTOPost> insert(@Valid @RequestBody CategoryDTOPost categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());

        Category saved = service.insert(category);
        CategoryDTOPost dto = new CategoryDTOPost(saved);

        URI location = getUri(saved.getId());
        return ResponseEntity.created(location).body(dto);
    }

    // Atualização via DTO
    @PutMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<CategoryDTOPost> update(@PathVariable("id") Long id,
                                                  @Valid @RequestBody CategoryDTOPost categoryDTO) {
        Category category = new Category();
        category.setId(id);
        category.setName(categoryDTO.name());

        Category updated = service.update(category, id);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CategoryDTOPost(updated));
    }

    // Delete
    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        return service.delete(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
