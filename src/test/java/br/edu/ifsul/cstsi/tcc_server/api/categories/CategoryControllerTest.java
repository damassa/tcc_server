package br.edu.ifsul.cstsi.tcc_server.api.categories;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.CustomPageImpl;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoryControllerTest extends BaseAPIIntegrationTest {
    private ResponseEntity<Category> getCategory(String url) {
        return get(url, Category.class);
    }

    private ResponseEntity <List<Category>> getCategories(String url) {
        var headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void selectAllEspera5Categorias() {
        // ACT + ASSERT
        assertEquals(5, getCategories("/api/v1/categories").getBody().size());
    }

    @Test
    public void selectByIdEsperaUmObjetoPorIdPesquisadoENotFoundParaIdInexistente() { //PASSOU
        // ACT + ASSERT
        assertNotNull(getCategory("/api/v1/categories/1"));
        assertNotNull(getCategory("/api/v1/categories/2"));
        assertNotNull(getCategory("/api/v1/categories/3"));
        assertNotNull(getCategory("/api/v1/categories/4"));
        assertNotNull(getCategory("/api/v1/categories/5"));
        assertEquals(HttpStatus.NOT_FOUND, getCategory("/api/v1/categories/99999").getStatusCode());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectByNomeEsperaUmObjetoPorNomePesquisado() { //PASSOU
        // ACT + ASSERT
        assertEquals(1, getCategories("/api/v1/categories/name/Sentai").getBody().size());
        assertEquals(1, getCategories("/api/v1/categories/name/Henshin").getBody().size());
        assertEquals(1, getCategories("/api/v1/categories/name/Metal").getBody().size());
        assertEquals(1, getCategories("/api/v1/categories/name/Ultra").getBody().size());
        assertEquals(1, getCategories("/api/v1/categories/name/Kamen").getBody().size());

        // ACT + ASSERT
        assertEquals(HttpStatus.NO_CONTENT, getCategory("/api/v1/categories/name/xxx").getStatusCode());
    }

    @Test
    public void testInsertEspera204CreatedE404NotFound() {// PASSOU
        // ARRANGE
        var category = new Category();
        category.setName("Teste");

        // ACT
        var response = post("/api/v1/categories", category, null);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ARRANGE
        var location = response.getHeaders().get("location").get(0);
        var c = getCategory(location).getBody();
        assertNotNull(c);
        assertEquals("Teste", c.getName());
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getCategory(location).getStatusCode());
    }

    @Test
    public void testUpdateEspera200OkE404NotFound() { // PASSOU
        // ARRANGE
        var category = new Category();
        category.setName("Teste");

        var responsePost = post("/api/v1/categories", category, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var c = getCategory(location).getBody();
        assertNotNull(c);
        assertEquals("Teste", c.getName());

        var category2 = new Category();
        category2.setName("Teste Alterado");

        // ACT
        var responsePUT = put(location, category2, Category.class);

        // ASSERT
        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Teste Alterado", responsePUT.getBody().getName());

        // ACT
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getCategory(location).getStatusCode());
    }

    @Test
    public void testDeleteEspera200OkE404NotFound() { //PASSOU
        // ARRANGE
        var category = new Category();
        category.setName("Teste");
        var responsePost = post("/api/v1/categories", category, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var c = getCategory(location).getBody();
        assertNotNull(c);
        assertEquals("Teste", c.getName());

        // ACT
        var responseDelete = delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getCategory(location).getStatusCode());
    }

    @Test
    public void testGetNotFoundEspera404NotFound() { //PASSOU
        // ARRANGE + ACT
        var response = getCategory("/api/v1/categories/99999");

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
