package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HistoryControllerTest extends BaseAPIIntegrationTest { // PASSOU
    private ResponseEntity<History> getHistory(String url) {
        return get(url, History.class);
    }

    @Test
    public void insert() {
        var historyDTOPost = new HistoryDTOPost(
                1L,
              2L,
              1L,
                1012L
        );

        var response = post("/api/v1/histories", historyDTOPost, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        System.out.println(location);
        var newHistory = getHistory(location).getBody();
        System.out.println(newHistory);
        assertNotNull(newHistory);
        assertEquals(1012L, newHistory.getPausedAt());
        delete(location, null);
    }

    @Test
    public void update() {
        var historyDTOPost = new HistoryDTOPost(
                1L,
                2L,
                1L,
                1012L
        );

        var response = post("/api/v1/histories", historyDTOPost, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var location = response.getHeaders().get("location").get(0);
        System.out.println(location);
        var newHistory = getHistory(location).getBody();
        System.out.println(newHistory);
        assertNotNull(newHistory);
        assertEquals(1012L, newHistory.getPausedAt());

        var historyDTOPut = new HistoryDTOPut(
                1L,
                2L,
                1L,
                1012L
        );

        var historyPUT = put(location, historyDTOPut, HistoryDTOResponse.class);
        assertEquals(HttpStatus.OK, historyPUT.getStatusCode());
        assertEquals(1042L, historyPUT.getBody().pausedAt());

        delete(location, null);

        assertEquals(HttpStatus.NOT_FOUND, getHistory(location).getStatusCode());
    }

    @Test
    public void delete() {
        var historyDTOPost = new HistoryDTOPost(
                1L,
                2L,
                1L,
                1012L
        );

        var response = post("/api/v1/histories", historyDTOPost, null);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        var newHistory = getHistory(location).getBody();
        System.out.println(newHistory);
        assertNotNull(newHistory);
        assertEquals(1012L, newHistory.getPausedAt());
        System.out.println(location);

        var responseDelete = delete(location, null);

        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getHistory(location).getStatusCode());
    }

    @Test
    public void getNotFound() {
        var response = getHistory("/api/v1/histories/99999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}