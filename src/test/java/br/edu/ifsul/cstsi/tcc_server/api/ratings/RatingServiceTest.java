package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
class RatingServiceTest {
    @Autowired
    private RatingService service;
    @Autowired
    private RatingRepository rep;

    @Test
    void insert() {
        var rating = new Rating();
        rating.setComment("Comentário teste.");
        rating.setStars(5);

        var r = service.insert(rating);
        assertNotNull(r);
        RatingKey id = r.getId();
        assertNotNull(id);
        r = rep.findById(id); //TODO: Tá esperando Long mas retorna RatingKey id
        assertNotNull(r);

        assertEquals("Comentário teste.", r.getComment());
        assertEquals(5, r.getStars());

        service.delete(id); //TODO: Mesma coisa da linha 26
        if (rep.findById(id).isPresent()) { //TODO: Mesma coisa da linha 32
            fail("A avaliação foi excluída.");
        }

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}