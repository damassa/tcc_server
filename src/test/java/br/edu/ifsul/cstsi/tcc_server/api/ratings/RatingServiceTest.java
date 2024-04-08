package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserService;
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
    @Autowired
    private SerieService serieService;
    @Autowired
    private UserRepository userRep;

    @Test
    void insert() {
        var rating = new Rating();
        rating.setId(new RatingKey(1L,2L));
        rating.setComment("Comentário teste.");
        rating.setStars(5);
        rating.setSerie(serieService.getSerieById(1L).get());
        rating.setUser(userRep.findById(2L).get());

        var r = service.insert(rating);
        assertNotNull(r);
        RatingKey id = r.getId();
        assertNotNull(id);
        var rt = rep.findRatingById(id);
        assertNotNull(rt);

        assertEquals("Comentário teste.", rt.get().getComment());
        assertEquals(5, rt.get().getStars());

        service.delete(id);
        if (rep.findRatingById(id).isPresent()) {
            fail("A avaliação foi excluída.");
        }

    }

    @Test
    void update() {
        var rating = new Rating();
        rating.setId(new RatingKey(1L,2L));
        rating.setComment("Comentário teste.");
        rating.setStars(5);
        rating.setSerie(serieService.getSerieById(1L).get());
        rating.setUser(userRep.findById(2L).get());

        var r = service.insert(rating);
        assertNotNull(r);
        RatingKey id = r.getId();
        assertNotNull(id);
        var rt = rep.findRatingById(id);
        assertNotNull(rt);

        assertEquals("Comentário teste.", rt.get().getComment());
        assertEquals(5, rt.get().getStars());

        r.setId(new RatingKey(1L, 2L));
        r.setComment("Comentário teste atualizado.");
        r.setStars(4);
        rating.setSerie(serieService.getSerieById(1L).get());
        rating.setUser(userRep.findById(2L).get());

        var r2 = service.update(rating);
        assertNotNull(r2);
    }

    @Test
    void delete() {
    }
}