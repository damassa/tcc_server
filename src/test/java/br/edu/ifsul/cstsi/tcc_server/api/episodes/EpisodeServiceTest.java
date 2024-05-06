package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
@ActiveProfiles("test")
class EpisodeServiceTest {
    @Autowired
    private EpisodeService service;
    @Autowired
    private EpisodeRepository rep;
    @Autowired
    private SerieService serieService;
    @Test
    void insert() {
        //ARRANGE
        var episode = new Episode();
        episode.setName("Episódio Teste");
        episode.setDuration(20);
        episode.setSerie(serieService.getSerieById(1L).get());
        episode.setHistories(null);

        //ACT
        var e = service.insert(episode);

        //ASSERT
        assertNotNull(e);
        Long id = e.getId();
        assertNotNull(id);
        e = rep.findById(id).get();
        assertNotNull(e);

        assertEquals("Episódio Teste", e.getName());
        assertEquals(20, e.getDuration());
        /*
        assertEquals(serieService.getSerieById(1L).get(), e.getSerie());
        assertNull(e.getHistories());
        */

        service.delete(id);
        if(rep.findById(id).isPresent()) {
            fail("O episódio foi excluído.");
        }
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        insert();
    }
}