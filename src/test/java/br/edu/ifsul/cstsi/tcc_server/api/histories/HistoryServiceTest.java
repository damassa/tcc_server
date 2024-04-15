package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeService;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
class HistoryServiceTest { //TODO: Revisar com o professor
    @Autowired
    private HistoryService service;
    @Autowired
    private HistoryRepository rep;
    @Autowired
    private EpisodeService episodeService;
    @Autowired
    private UserRepository userRepository;
    @Test
    void insert() {
        var history = new History();
        history.setId(new HistoryKey(1L, 2L));
        //history.setEpisode(episodeService);
        history.setPausedAt(15:23:01);

        var h = service.insert(history);
        assertNotNull(h);
        HistoryKey id = h.getId();
        assertNotNull(id);
        var hr = rep.findHistoryById(id);
        assertNotNull(hr);

        assertEquals(15:23:01, hr.get().getPausedAt());

        service.delete(id);
        if(rep.findHistoryById(id).isPresent()) {
            fail("O histórico foi excluído.");
        }
    }

    @Test
    void update() {
        var history = new History();
        history.setId(new HistoryKey(1L, 2L));
        history.setPausedAt();
    }

    @Test
    void delete() {
    }
}