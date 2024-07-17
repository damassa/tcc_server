package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    @Test
    public void insert() { // TODO: Revisar sexta
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
        Long id = episode.getId();
        var e = episodeRepository.findById(id).get();
        assertNotNull(e);
        assertEquals("Episódio Teste Insert", e.getName());
        assertEquals(21, e.getDuration());

        episodeService.delete(id);
        if(episodeRepository.findById(id).isPresent()) {
            fail("O episódio foi excluído.");
        }
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