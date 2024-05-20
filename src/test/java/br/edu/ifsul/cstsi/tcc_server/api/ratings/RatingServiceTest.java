package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
@ActiveProfiles("test")
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
        //ARRANGE
        var rating = new Rating();
        rating.setId(new RatingKey(1L,2L));
        rating.setComment("Comentário teste.");
        rating.setStars(5);
        rating.setSerie(serieService.getSerieById(1L).get());
        rating.setUser(userRep.findById(2L).get());

        var r = service.insert(rating);
        assertNotNull(r);

        r.setComment("Comentário teste atualizado.");
        r.setStars(4);
        r.setSerie(serieService.getSerieById(1L).get());
        r.setUser(userRep.findById(2L).get());

        //ACT
        var r2 = service.update(r);

        //ASSERT
        assertNotNull(r2);
        assertEquals("Comentário teste atualizado.", r2.getComment());
        assertEquals(4, r2.getStars());

        service.delete(r2.getId());
        if (rep.findRatingById(r2.getId()).isPresent()) {
            fail("A avaliação foi excluída.");
        }
    }

    @Test
    void delete() {
        var rating = new Rating();
        rating.setId(new RatingKey(1L,2L));
        rating.setComment("Comentário teste pra ser deletado.");
        rating.setStars(2);
        rating.setSerie(serieService.getSerieById(1L).get());
        rating.setUser(userRep.findById(2L).get());

        var r = service.insert(rating);
        assertNotNull(r);
        RatingKey id = r.getId();
        assertNotNull(id);
        var rt = rep.findRatingById(id);
        assertNotNull(rt);

        assertEquals("Comentário teste pra ser deletado.", rt.get().getComment());
        assertEquals(2, rt.get().getStars());

        service.delete(id);

        if (rep.findRatingById(id).isPresent()) {
            fail("A avaliação foi excluída.");
        }
    }
}