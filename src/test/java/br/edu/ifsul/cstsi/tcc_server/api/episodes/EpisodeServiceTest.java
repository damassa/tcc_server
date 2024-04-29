package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.histories.HistoryRepository;
import br.edu.ifsul.cstsi.tcc_server.api.histories.HistoryService;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieRepository;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

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
    @Autowired
    private HistoryRepository historyRepository;
    @Test
    void insert() {
        var episode = new Episode();
        episode.setName("Episódio Teste");
        episode.setDuration(20);
        episode.setSerie(serieService.getSerieById(1L).get());
        //episode.setHistories();
        //TODO: Tratar histórico no episódio

        /*var e = service.insert(episode);
        assertNotNull(e);
        Long id = e.getId();
        assertNotNull(id);
        e = rep.findById(id).get();
        assertNotNull(e);

        assertEquals("Episódio Teste", e.getName());

        service.delete(id);
        if(rep.findById(id).isPresent()) {
            fail("O episódio foi excluído.");
        }*/
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}