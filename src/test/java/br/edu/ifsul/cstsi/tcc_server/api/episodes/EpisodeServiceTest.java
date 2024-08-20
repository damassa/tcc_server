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


        service.delete(id);
        if(rep.findById(id).isPresent()) {
            fail("O episódio foi excluído.");
        }
    }

    @Test
    void update() {
        // ARRANGE
        var epOriginal = rep.findById(1L).get(); //episódio original na base de dados
        var epMock = new Episode();
        epMock.setId(epOriginal.getId());
        epMock.setName("Teste");
        epMock.setDuration(20);

        // ACT
        var episodeAltered = service.update(epMock, epMock.getId());

        // ASSERT
        assertNotNull(episodeAltered);
        assertEquals("Teste", episodeAltered.getName());
        assertEquals(20, episodeAltered.getDuration());

        //volta ao valor original (para manter a consistência do banco de dados)
        var episodeOriginal = service.update(epOriginal, epOriginal.getId());
        assertNotNull(episodeOriginal);
    }

    @Test
    void delete() {
        insert();
    }
}