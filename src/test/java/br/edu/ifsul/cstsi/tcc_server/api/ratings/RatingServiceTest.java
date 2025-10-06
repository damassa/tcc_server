package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.categories.Category;
import br.edu.ifsul.cstsi.tcc_server.api.categories.CategoryService;
import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieDTOPost;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class)
@ActiveProfiles("test")
class RatingServiceTest {

    @Autowired
    private RatingService service;
    @Autowired
    private RatingRepository rep;
    @Autowired
    private SerieService serieService;
    @Autowired
    private UserRepository userRep;
    @Autowired
    private CategoryService categoryService;

    // Cria categoria e retorna a entidade
    private Category criarCategoriaTeste() {
        Category categoria = new Category();
        categoria.setName("Categoria Teste");
        return categoryService.insert(categoria);
    }

    // Cria série vinculada a categoria
    private Serie criarSerieTeste() {
        Category categoria = criarCategoriaTeste();
        SerieDTOPost serieDto = new SerieDTOPost(
                "Série Teste",
                "Trama de teste",
                2020,
                "imagem.jpg",
                "bigImage.jpg",
                "https://youtube.com/teste",
                categoria.getId()
        );
        return serieService.insert(serieDto);
    }

    // Cria usuário de teste
    private User criarUsuarioTeste() {
        User user = new User();
        user.setName("Usuário Teste");
        user.setEmail("teste@example.com");
        user.setPassword("123456");
        return userRep.save(user);
    }

    @Test
    void insert() {
        Serie serie = criarSerieTeste();
        User user = criarUsuarioTeste();

        RatingDTOPost dto = new RatingDTOPost(user.getId(), serie.getId(), 5);
        Rating saved = service.insert(dto);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(5, saved.getStars());

        // Cleanup
        service.delete(saved.getId());
        assertFalse(rep.findById(saved.getId()).isPresent());
    }

    @Test
    void update() {
        Serie serie = criarSerieTeste();
        User user = criarUsuarioTeste();

        Rating saved = service.insert(new RatingDTOPost(user.getId(), serie.getId(), 4));
        RatingDTOPut updateDto = new RatingDTOPut(saved.getId(), user.getId(), serie.getId(), 3);
        Rating updated = service.update(updateDto);

        assertNotNull(updated);
        assertEquals(3, updated.getStars());

        // Cleanup
        service.delete(updated.getId());
        assertFalse(rep.findById(updated.getId()).isPresent());
    }

    @Test
    void delete() {
        Serie serie = criarSerieTeste();
        User user = criarUsuarioTeste();

        Rating saved = service.insert(new RatingDTOPost(user.getId(), serie.getId(), 2));
        service.delete(saved.getId());

        assertFalse(rep.findById(saved.getId()).isPresent());
    }
}
