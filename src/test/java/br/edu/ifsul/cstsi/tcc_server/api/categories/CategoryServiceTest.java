package br.edu.ifsul.cstsi.tcc_server.api.categories;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class)
class CategoryServiceTest {
    @Autowired
    private CategoryService service;
    @Test
    void getCategories() {
        var categories = service.getCategories();
        assertEquals(2, categories.size());
    }

    @Test
    void getCategoryById() {
        var category = service.getCategoryById(2L);
        assertNotNull(category);
        assertEquals("Super Sentai", category.get().getName());
    }

    @Test
    void getCategoriesByName() {
        assertEquals(1, service.getCategoriesByName("Super").size());
        assertEquals(1, service.getCategoriesByName("Ultraman").size());
    }

    @Test
    void insert() {
        var category = new Category();
        category.setName("Categoria Teste");

        var c = service.insert(category);
        assertNotNull(c);
        Long id = c.getId();
        assertNotNull(id);
        c = service.getCategoryById(id).get();
        assertNotNull(c);

        assertEquals("Categoria Teste", c.getName());

        service.delete(id);
        if(service.getCategoryById(id).isPresent()) {
            fail("A categoria foi excluída.");
        }
    }

    @Test
    void update() {
        var c = service.getCategoryById(2L).get();
        var name = c.getName();
        c.setName("Categoria Teste Modificada");
        var c2 = service.update(c, c.getId());
        assertNotNull(c2);
        assertEquals("Categoria Teste Modificada", c2.getName());

        c.setName(name);
        c2 = service.update(c, c.getId());
        assertNotNull(c2);
    }

    @Test
    void delete() {
        var category = new Category();
        category.setName("Categoria Teste");

        var c = service.insert(category);

        assertNotNull(c);

        Long id = c.getId();
        assertNotNull(id);
        c = service.getCategoryById(id).get();
        assertNotNull(c);

        service.delete(id);
        if(service.getCategoryById(id).isPresent()) {
            fail("A categoria não foi excluída.");
        }
    }
}