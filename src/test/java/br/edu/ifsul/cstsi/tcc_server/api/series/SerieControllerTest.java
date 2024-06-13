package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SerieControllerTest extends BaseAPIIntegrationTest {

    private ResponseEntity<Serie> getSerie(String url) {
        return get(url, Serie.class);
    }

    private ResponseEntity <List<Serie>> getSeries(String url) {
        var headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),

                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void selectAllEspera2Series() {
        // ACT + ASSERT
        assertEquals(2, getSeries("/api/v1/series").getBody().size());
    }

    @Test
    public void selectByIdEsperaUmObjetoPorIdPesquisadoENotFoundParaIdInexistente() {
        // ACT + ASSERT
        assertNotNull(getSerie("/api/v1/series/1"));
        assertNotNull(getSerie("/api/v1/series/2"));
        assertEquals(HttpStatus.NOT_FOUND, getSerie("/api/v1/series/99999").getStatusCode());
    }

    @Test
    public void selectByNameEsperaUmObjetoPorNomePesquisado() {
        // ACT + ASSERT
        assertEquals(1, getSeries("/api/v1/series/Gorenger").getBody().size());
        assertEquals(1, getSeries("/api/v1/series/Dengekitai").getBody().size());

        // ACT + ASSERT
        assertEquals(HttpStatus.NO_CONTENT, getSerie("/api/v1/series/name/xxx").getStatusCode());
    }

    @Test
    public void insertEspera204CreatedE404NotFound() {
        // ARRANGE
        var serie = new Serie();
        serie.setName("Série Teste");
        serie.setYear(2024);
        serie.setOpening_video("ASDASDASDASd");
        serie.setImage("ASDAFDSAF");
        serie.setPlot("Uma série legal.");
        serie.setBigImage("FGFSDGFSDG");

        // ACT
        var response = post("/api/v1/series", serie, null);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ARRANGE
        var location = response.getHeaders().get("location").get(0);
        var s = getSerie(location).getBody();
        assertNotNull(s);
        assertEquals("Série Teste", s.getName());
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getSerie(location).getStatusCode());
    }

    @Test
    public void updateEspera200OkE404NotFound() {
        // ARRANGE
        var serie = new Serie();
        serie.setName("Série Teste");
        serie.setYear(2024);
        serie.setOpening_video("ASDASDASDASd");
        serie.setImage("ASDAFDSAF");
        serie.setPlot("Uma série legal.");
        serie.setBigImage("FGFSDGFSDG");

        var responsePost = post("/api/v1/series", serie, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var s = getSerie(location).getBody();
        assertNotNull(s);
        assertEquals("Série Teste", s.getName());

        var serie2 = new Serie();
        serie.setName("Série Teste Modificada");
        serie.setYear(2023);
        serie.setOpening_video("ASDASDASDASd ALTERADO");
        serie.setImage("ASDAFDSAF ALTERADO");
        serie.setPlot("Uma série legal só que alterada.");
        serie.setBigImage("FGFSDGFSDG ALTERADO");

        // ACT
        var responsePUT = put(location, serie2, Serie.class);

        //ASSERT
        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Série Teste Modificada", responsePUT.getBody().getName());
        assertEquals(2023, responsePUT.getBody().getYear());
        assertEquals("ASDASDASDASd ALTERADO", responsePUT.getBody().getOpening_video());
        assertEquals("ASDAFDSAF ALTERADO", responsePUT.getBody().getImage());
        assertEquals("Uma série legal só que alterada.", responsePUT.getBody().getPlot());
        assertEquals("FGFSDGFSDG ALTERADO", responsePUT.getBody().getBigImage());

        // ACT
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getSerie(location).getStatusCode());
    }

    @Test
    public void deleteEspera200OkE404NotFound() {
        // ARRANGE
        var serie = new Serie();
        serie.setName("Série Teste");
        serie.setYear(2024);
        serie.setOpening_video("ASDASDASDASd");
        serie.setImage("ASDAFDSAF");
        serie.setPlot("Uma série legal.");
        serie.setBigImage("FGFSDGFSDG");
        var responsePost = post("/api/v1/series", serie, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var s = getSerie(location).getBody();
        assertNotNull(s);
        assertEquals("Série Teste", s.getName());

        // ACT
        var responseDelete = delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getSerie(location).getStatusCode());
    }

    @Test
    public void testGetNotFoundEspera404NotFound() {
        // ARRANGE + ACT
        var response = getSerie("/api/v1/series/99999");

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}