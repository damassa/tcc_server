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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoryControllerTest extends BaseAPIIntegrationTest {
    private ResponseEntity<Category> getCategory(String url) {
        return get(url, Category.class);
    }

    private ResponseEntity<CustomPageImpl<Category>> getCategoriesPageable(String url) {
        var headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    @DisplayName("Espera uma página, testa se tem 5 objetos, busca por página, de tamanho 5 e testa se tem 5 objetos")
    public void selectAllEsperaUmaPaginaCom5ObjetosEUmaPaginaDe5Objetos() { //O nome do método de teste é importante porque deve transmitir a essência do que ele verifica. Este não é um requisito técnico, mas sim uma oportunidade de capturar informações
        // ACT
        var page = getCategoriesPageable("/api/v1/categories").getBody();

        // ASSERT (testa se retorna a quantidade de dados esperada)
        assertNotNull(page);
        assertEquals(5, page.stream().count());

        // ACT
        page = getCategoriesPageable("/api/v1/categories?page=0&size=5").getBody();

        // ASSERT (testa se retorna o tamanho de página solicitado)
        assertNotNull(page);
        assertEquals(5, page.stream().count());
    }

    @Test //esta anotação JUnit sinaliza que este método é um caso de teste
    public void selectByNomeEsperaUmObjetoPorNomePesquisado() {
        // ACT + ASSERT
        assertEquals(1, getCategory("/api/v1/categories/name/sentai").getBody().size());
        assertEquals(1, getCategory("/api/v1/categories/name/henshin").getBody().size());
        assertEquals(1, getCategory("/api/v1/categories/name/metal").getBody().size());
        assertEquals(1, getCategory("/api/v1/categories/name/ultra").getBody().size());
        assertEquals(1, getCategory("/api/v1/categories/name/kamen").getBody().size());

        // ACT + ASSERT
        assertEquals(HttpStatus.NO_CONTENT, getCategory("/api/v1/produtos/nome/xxx").getStatusCode());
    }
}
