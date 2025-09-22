package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EpisodeControllerTest extends BaseAPIIntegrationTest {
    @Autowired
    private SerieService serieService;

    private ResponseEntity<Episode> getEpisode(String url) {
        return get(url, Episode.class);
    }

    @Test
    public void insert() { // PASSOU
        // ARRANGE
        var episode = new EpisodeDTOPost(
            "Episódio Teste Insert",
            21,
            "link teste",
                1L
        );


        //ACT
        var response = post("/api/v1/episodes", episode, null);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ARRANGE
        var location = response.getHeaders().get("location").get(0);
        var newEp = getEpisode(location).getBody();
        assertNotNull(newEp);
        assertEquals("Episódio Teste Insert", newEp.getName());
        assertEquals(21, newEp.getDuration());
        delete(location, null);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, getEpisode(location).getStatusCode());
    }

    @Test
    public void update() { // PASSOU
        // ARRANGE
        var serie = serieService.getSerieById(1L);
        var episode = new EpisodeDTOPost(
                "Episódio Teste Update",
                22,
                "Link lalala",
                serie.id()
        );
//        episode.setName("Episódio Teste Update");
//        episode.setDuration(22);
//        episode.setSerie(serieService.getSerieById(1L).get());

        // ACT
        var response = post("/api/v1/episodes", episode, null);

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // ARRANGE
        var location = response.getHeaders().get("location").get(0);
        var newEp = getEpisode(location).getBody();
        assertNotNull(newEp);
        assertEquals("Episódio Teste Update", newEp.getName());
        assertEquals(22, newEp.getDuration());
        assertEquals("Link lalala", newEp.getLink());

        var epModificado = new EpisodeDTOPut(
                "Episódio Update Modificado",
                23,
                "Link Modificado lalala",
                serie.id()
        );
//        var epModificado = new Episode();
//        epModificado.setName("Episódio Update Modificado");
//        epModificado.setDuration(23);
//        epModificado.setSerie(serieService.getSerieById(1L).get());

        var responsePUT = put(location, epModificado, EpisodeDTOGet.class);
        System.out.println(responsePUT);

        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Episódio Update Modificado", responsePUT.getBody().name());
        assertEquals(23, responsePUT.getBody().duration());

        delete(location, null);

        assertEquals(HttpStatus.NOT_FOUND, getEpisode(location).getStatusCode());
    }

    @Test
    void delete() { // PASSOU
        var episode = new Episode();
        episode.setName("Episódio Teste Delete");
        episode.setDuration(25);

        var response = post("/api/v1/episodes", episode, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var location = response.getHeaders().get("location").get(0);
        var newEp = getEpisode(location).getBody();
        assertNotNull(newEp);
        assertEquals("Episódio Teste Delete", newEp.getName());

        var responseDelete = delete(location, null);
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getEpisode(location).getStatusCode());
    }
}