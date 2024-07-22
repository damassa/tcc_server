package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
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
    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private EpisodeService episodeService;
    @Autowired
    private EpisodeRepository episodeRepository;

    private ResponseEntity<Episode> getEpisode(String url) {
        return get(url, Episode.class);
    }

    @Test
    public void insert() { // PASSOU
        // ARRANGE
        var episode = new Episode();
        episode.setName("Episódio Teste Insert");
        episode.setDuration(21);
        episode.setSerie(serieService.getSerieById(1L).get());
        episode.setHistories(null);

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
    public void update() {
        // ARRANGE
        var episode = new Episode();
        episode.setName("Episódio Teste Update");
        episode.setDuration(21);
        episode.setSerie(serieService.getSerieById(1L).get());
        episode.setHistories(null);

        var response = post("/api/v1/episodes", episode, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var location = response.getHeaders().get("location").get(0);
        Long id = episode.getId();
    }

    @Test
    void delete() {
    }
}