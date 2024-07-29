package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HistoryControllerTest extends BaseAPIIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EpisodeRepository episodeRepository;

    private ResponseEntity<History> getHistory(String url) {
        return get(url, History.class);
    }

    @Test
    public void insert() { //TODO
        History history = new History();
        history.setId(new HistoryKey(1L, 2L));
        history.setUser(userRepository.findById(2L).get());
        history.setEpisode(episodeRepository.findById(1L).get());
        history.setPausedAt(LocalTime.parse("16:42"));

        var response = post("/api/v1/histories", history, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        var newHistory = getHistory(location).getBody();
        assertNotNull(newHistory);
        assertEquals(LocalTime.parse("15:23"), newHistory.getPausedAt());
        delete(location, null);
    }

    @Test
    public void update() { //TODO
        History history = new History();
        history.setId(new HistoryKey(1L, 2L));
        history.setUser(userRepository.findById(2L).get());
        history.setEpisode(episodeRepository.findById(1L).get());
        history.setPausedAt(LocalTime.parse("22:22"));

        var response = post("/api/v1/histories", history, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        var newHistory = getHistory(location).getBody();
        assertNotNull(newHistory);
        assertEquals(LocalTime.parse("22:22"), newHistory.getPausedAt());

        var updatedHistory = new History();
        updatedHistory.setId(new HistoryKey(1L, 2L));
        updatedHistory.setUser(userRepository.findById(2L).get());
        updatedHistory.setEpisode(episodeRepository.findById(1L).get());
        updatedHistory.setPausedAt(LocalTime.parse("23:23"));

        var responsePUT = put("/api/v1/histories", updatedHistory, History.class);
        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals(LocalTime.parse("23:23"), responsePUT.getBody().getPausedAt());

        delete(location, null);

        assertEquals(HttpStatus.NOT_FOUND, getHistory(location).getStatusCode());
    }

    @Test
    public void delete() { //TODO
        History history = new History();
        history.setId(new HistoryKey(1L, 2L));
        history.setUser(userRepository.findById(2L).get());
        history.setEpisode(episodeRepository.findById(1L).get());
        history.setPausedAt(LocalTime.parse("22:23"));

        var response = post("/api/v1/histories", history, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        var newHistory = getHistory(location).getBody();
        assertNotNull(newHistory);
        assertEquals(LocalTime.parse("22:23"), newHistory.getPausedAt());

        var responseDelete = delete(location, null);
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getHistory(location).getStatusCode());
    }
}