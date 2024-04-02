package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
class EpisodeServiceTest {
    @Autowired
    private EpisodeService service;
    @Autowired
    private EpisodeRepository rep;
    @Test
    void insert() {
        var episode = new Episode();

        var e = service.insert(episode);
        assertNotNull(e);
        Long id = e.getId();
        assertNotNull(id);
        e = rep.findById(id).get();
        assertNotNull(e);

        assertEquals("Episódio Teste", e.getName());

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
    }
}