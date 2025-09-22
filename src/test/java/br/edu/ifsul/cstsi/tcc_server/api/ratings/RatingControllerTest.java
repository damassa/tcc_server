package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RatingControllerTest extends BaseAPIIntegrationTest {

    private ResponseEntity<RatingDTOResponse> getRating(String url) {
        return get(url, RatingDTOResponse.class);
    }

    @Test
    void insert() {
        var ratingDTOPost = new RatingDTOPost(
                2L, // idUser
                1L, // idSerie
                "Comentário teste insert",
                5
        );

        var response = post("/api/v1/ratings", ratingDTOPost, RatingDTOResponse.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().getLocation().toString();
        var newRating = getRating(location).getBody();
        assertNotNull(newRating);
        assertEquals(5, newRating.stars());
        assertEquals("Comentário teste insert", newRating.comment());

        delete(location, null);
        assertEquals(HttpStatus.NOT_FOUND, getRating(location).getStatusCode());
    }

    @Test
    void update() {
        // Criar rating inicial
        var ratingDTOPost = new RatingDTOPost(2L, 1L, "Comentário teste update", 5);
        var response = post("/api/v1/ratings", ratingDTOPost, RatingDTOResponse.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().getLocation().toString();
        var rDTO = getRating(location).getBody();
        assertNotNull(rDTO);

        // Atualizar rating
        var ratingDTOPut = new RatingDTOPut(
                rDTO.id(), // usar o id criado
                rDTO.idUser(),
                rDTO.idSerie(),
                "Comentário teste update modificado",
                4
        );

        var responsePUT = put(location, ratingDTOPut, RatingDTOResponse.class);
        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Comentário teste update modificado", responsePUT.getBody().comment());
        assertEquals(4, responsePUT.getBody().stars());

        delete(location, null);
        assertEquals(HttpStatus.NOT_FOUND, getRating(location).getStatusCode());
    }

    @Test
    void delete() {
        var ratingDTOPost = new RatingDTOPost(2L, 1L, "Comentário teste delete", 3);
        var response = post("/api/v1/ratings", ratingDTOPost, RatingDTOResponse.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().getLocation().toString();
        var newRating = getRating(location).getBody();
        assertNotNull(newRating);
        assertEquals("Comentário teste delete", newRating.comment());
        assertEquals(3, newRating.stars());

        var responseDelete = delete(location, null);
        assertEquals(HttpStatus.NO_CONTENT, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getRating(location).getStatusCode());
    }

    @Test
    public void getNotFound() {
        var response = getRating("/api/v1/ratings/99999");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
