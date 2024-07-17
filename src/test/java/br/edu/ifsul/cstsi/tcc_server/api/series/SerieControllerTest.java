package br.edu.ifsul.cstsi.tcc_server.api.series;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SerieControllerTest extends BaseAPIIntegrationTest {

    private ResponseEntity<SerieDTOResponse> getSerie(String url) {
        return get(url, SerieDTOResponse.class);
    }

    public ResponseEntity<CustomPageImpl<SerieDTOResponse>> getSeriesPageable(String url) {
        var headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<CustomPageImpl<SerieDTOResponse>>() {});
    }

    private ResponseEntity <List<SerieDTOResponse>> getSeries(String url) {
        var headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    @DisplayName("Espera uma página, testa se tem 5 objetos, busca por página, de tamanho 5, e testa se tem 5 objetos")
    public void selectAllEsperaUmaPaginaCom5ObjetosEUmaPaginaDe5Objetos() { // PASSOU
        // ACT
        var page = getSeriesPageable("/api/v1/series").getBody();

        // ASSERT
        assertNotNull(page);
        assertEquals(5, page.stream().count());
    }

    @Test
    public void selectByIdEsperaUmObjetoPorIdPesquisadoENotFoundParaIdInexistente() { // PASSOU
        // ACT + ASSERT
        assertNotNull(getSerie("/api/v1/series/1"));
        assertNotNull(getSerie("/api/v1/series/2"));
        assertNotNull(getSerie("/api/v1/series/3"));
        assertNotNull(getSerie("/api/v1/series/4"));
        assertNotNull(getSerie("/api/v1/series/5"));
        assertEquals(HttpStatus.NOT_FOUND, getSerie("/api/v1/series/99999").getStatusCode());
    }

    @Test
    public void selectByNameEsperaUmObjetoPorNomePesquisado() { // PASSOU
        // ACT + ASSERT
        assertEquals(1, getSeries("/api/v1/series/name/Gorenger").getBody().size());
        assertEquals(1, getSeries("/api/v1/series/name/Dengekitai").getBody().size());
        assertEquals(1, getSeries("/api/v1/series/name/Battle").getBody().size());
        assertEquals(1, getSeries("/api/v1/series/name/Denshi").getBody().size());
        assertEquals(1, getSeries("/api/v1/series/name/Sun").getBody().size());

        // ACT + ASSERT
        assertEquals(HttpStatus.NO_CONTENT, getSeries("/api/v1/series/name/xxx").getStatusCode());
    }

    @Test
    public void insertEspera204CreatedE404NotFound() { //PASSOU
        // ARRANGE
        var SerieDTOPost = new SerieDTOPost(
                "Série Teste Insert",
                "Sinopse Teste Insert",
                2025,
                "asdasdasd Insert",
                "asdasdasdas Insert",
                "https://www.youtube.com/watch?v=Z0DO0XyS8Ko"
        );

        // ACT
        var response = post("/api/v1/series", SerieDTOPost, null);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ARRANGE
        var location = response.getHeaders().get("location").get(0);
        var s = getSerie(location).getBody();
        assertNotNull(s);
        assertEquals("Série Teste Insert", s.name());
        assertEquals("Sinopse Teste Insert", s.plot());
        assertEquals(2025, s.year());
        assertEquals("asdasdasd Insert", s.image());
        assertEquals("asdasdasdas Insert", s.bigImage());
        assertEquals("https://www.youtube.com/watch?v=Z0DO0XyS8Ko", s.opening_video());
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getSerie(location).getStatusCode());
    }
    @Test
    public void updateEspera200OkE404NotFound() {
        // ARRANGE
        var SerieDTOPost = new SerieDTOPost(
                "Série Teste Update",
                "Sinopse Teste Update",
                2025,
                "asdasdasd Update",
                "asdasdasdas Update",
                "https://www.youtube.com/watch?v=Z0DO0XyS8Ko"
        );

        var responsePost = post("/api/v1/series", SerieDTOPost, null);
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        var location = responsePost.getHeaders().get("location").get(0);
        var sDto = getSerie(location).getBody();
        assertNotNull(sDto);
        assertEquals("Série Teste Update", sDto.name());
        assertEquals("Sinopse Teste Update", sDto.plot());
        assertEquals(2025, sDto.year());
        assertEquals("asdasdasd Update", sDto.image());
        assertEquals("asdasdasdas Update", sDto.bigImage());
        assertEquals("https://www.youtube.com/watch?v=Z0DO0XyS8Ko", sDto.opening_video());

        var serieDTOPut = new SerieDTOPut(
                "Série Teste modificada",
                "Sinopse Teste modificada",
                2024,
                "asdasdasdA",
                "asdasdasdasA",
                "https://www.youtube.com/watch?v=aRsWk4JZa5k"
        );

        // ACT
        var responsePUT = put(location, serieDTOPut, SerieDTOResponse.class);

        // ASSERT
        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Série Teste modificada", responsePUT.getBody().name());
        assertEquals("Sinopse Teste modificada", responsePUT.getBody().plot());
        assertEquals(2024, responsePUT.getBody().year());
        assertEquals("asdasdasdA", responsePUT.getBody().image());
        assertEquals("asdasdasdasA", responsePUT.getBody().bigImage());
        assertEquals("https://www.youtube.com/watch?v=aRsWk4JZa5k", responsePUT.getBody().opening_video());

        // ACT
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getSerie(location).getStatusCode());
    }

    @Test
    public void deleteEspera200OkE404NotFound() { //PASSOU
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
        assertEquals("Série Teste", s.name());

        // ACT
        var responseDelete = delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getSerie(location).getStatusCode());
    }

    @Test
    public void testGetNotFoundEspera404NotFound() { //PASSOU
        // ARRANGE + ACT
        var response = getSerie("/api/v1/series/99999");

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}