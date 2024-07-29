package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeRepository;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeService;
import br.edu.ifsul.cstsi.tcc_server.api.ratings.RatingKey;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
@ActiveProfiles("test")
class HistoryServiceTest {
    @Autowired
    private HistoryService service;
    @Autowired
    private HistoryRepository rep;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EpisodeRepository episodeRep;
    @Test
    void insert() {
        var history = new History();
        history.setId(new HistoryKey(1L, 2L));
        history.setUser(userRepository.findById(2L).get());
        history.setEpisode(episodeRep.findById(1L).get());
        history.setPausedAt(LocalTime.parse("15:23"));

        var h = service.insert(history);
        assertNotNull(h);
        HistoryKey id = h.getId();
        assertNotNull(id);
        var hr = rep.findHistoryById(id);
        assertNotNull(hr);

        assertEquals(LocalTime.parse("15:23"), hr.get().getPausedAt());

        service.delete(id);
        if(rep.findHistoryById(id).isPresent()) {
            fail("O histórico foi excluído.");
        }
    }

    @Test
    void update() {
        //ARRANGE
        var history = new History();
        history.setId(new HistoryKey(1L, 2L));
        history.setPausedAt(LocalTime.parse("19:24"));
        history.setEpisode(episodeRep.findById(1L).get());
        history.setUser(userRepository.findById(2L).get());

        var h = service.insert(history);
        assertNotNull(h);

        h.setId(new HistoryKey(1L, 2L));
        h.setPausedAt(LocalTime.parse("18:12"));
        h.setEpisode(episodeRep.findById(1L).get());
        h.setUser(userRepository.findById(2L).get());

        //ACT
        var h2 = service.update(h);

        //ASSERT
        assertNotNull(h2);
        assertEquals(LocalTime.parse("18:12"), h2.getPausedAt());

        service.delete(h2.getId());
        if (rep.findHistoryById(h2.getId()).isPresent()) {
            fail("O histórico foi excluído.");
        }
    }

    @Test
    void delete() {
        insert();
    }
}