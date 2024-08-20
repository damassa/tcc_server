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
public class RatingControllerTest extends BaseAPIIntegrationTest { // PASSOU

    private ResponseEntity<Rating> getRating(String url) {
        return get(url, Rating.class);
    }

    @Test
    void insert() {
        var ratingDTOPost = new RatingDTOResponse(
                1L,
                2L,
                1L,
                5,
                "Comentário teste insert"
        );

        var response = post("/api/v1/ratings", ratingDTOPost, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        System.out.println(location);
        var newRating = getRating(location).getBody();
        System.out.println(newRating);
        assertNotNull(newRating);
        assertEquals(5, newRating.getStars());
        assertEquals("Comentário teste insert", newRating.getComment());

        delete(location, null);
//        Rating rating = new Rating();
//        rating.setId(new RatingKey(1L, 2L));
//        rating.setComment("Comentário teste insert.");
//        rating.setStars(4);
//        rating.setSerie(serieService.getSerieById(1L).get());
//        rating.setUser(userRepository.findById(2L).get());
//
//        var response = post("/api/v1/ratings", rating, null);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//
//        var location = response.getHeaders().get("location").get(0);
//        var newRating = getRating(location).getBody();
//        assertNotNull(newRating);
//        assertEquals("Comentário teste insert.", newRating.getComment());
//        assertEquals(4, newRating.getStars());
//        delete(location, null);
    }

    @Test
    void update() {
        var ratingDTOPost = new RatingDTOPost(
          1L,
          2L,
          1L,
          "Comentário teste update",
          5
        );
        var response = post("/api/v1/ratings", ratingDTOPost, null);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var location = response.getHeaders().get("location").get(0);
        var rDTO = getRating(location).getBody();
        assertNotNull(rDTO);
        assertEquals("Comentário teste update", rDTO.getComment());
        assertEquals(5, rDTO.getStars());

        var ratingDTOPut = new RatingDTOPut(
          1L,
          2L,
          1L,
          "Comentário teste update modificado",
          4
        );

        var responsePUT = put(location, ratingDTOPut, RatingDTOResponse.class);

        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
        assertEquals("Comentário teste update modificado", responsePUT.getBody().comment());
        assertEquals(4, responsePUT.getBody().stars());

        delete(location, null);

        assertEquals(HttpStatus.NOT_FOUND, getRating(location).getStatusCode());

//        Rating rating = new Rating();
//        rating.setId(new RatingKey(1L, 2L));
//        rating.setComment("Comentário teste update.");
//        rating.setStars(3);
//        rating.setSerie(serieService.getSerieById(1L).get());
//        rating.setUser(userRepository.findById(2L).get());
//
//        var response = post("/api/v1/ratings", rating, null);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//
//        var location = response.getHeaders().get("location").get(0);
//        var newRating = getRating(location).getBody();
//        assertNotNull(newRating);
//        assertEquals("Comentário teste update.", newRating.getComment());
//        assertEquals(3, newRating.getStars());
//
//        var ratingModificado = new Rating();
//        ratingModificado.setId(new RatingKey(1L, 2L));
//        ratingModificado.setSerie(serieService.getSerieById(1L).get());
//        ratingModificado.setUser(userRepository.findById(2L).get());
//        ratingModificado.setComment("Comentário teste update modificado.");
//        ratingModificado.setStars(5);
//
//        var responsePUT = put("/api/v1/ratings", ratingModificado, Rating.class);
//        assertEquals(HttpStatus.OK, responsePUT.getStatusCode());
//        assertEquals("Comentário teste update modificado.", responsePUT.getBody().getComment());
//        assertEquals(5, responsePUT.getBody().getStars());
//
//        delete(location, null);
//
//        assertEquals(HttpStatus.NOT_FOUND, getRating(location).getStatusCode());
    }

    @Test
    void delete() {
        var ratingDTOPost = new RatingDTOPost(
                1L,
                2L,
                1L,
                "Comentário teste delete.",
                3
        );

        var response = post("/api/v1/ratings", ratingDTOPost, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var location = response.getHeaders().get("location").get(0);
        var newRating = getRating(location).getBody();
        assertNotNull(newRating);
        assertEquals("Comentário teste delete.", newRating.getComment());
        assertEquals(3, newRating.getStars());

        var responseDelete = delete(location, null);
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, getRating(location).getStatusCode());
    }

    @Test
    public void getNotFound() {
        var response = getRating("/api/v1/ratings/99999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}