package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.EpisodeRepository;
import br.edu.ifsul.cstsi.tcc_server.api.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

    // 🔧 Converte "HH:mm" para segundos
    private Long toSeconds(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (long) (hours * 3600 + minutes * 60);
    }

    // 🔧 Converte segundos para "HH:mm"
    private String toTimeString(Long seconds) {
        if (seconds == null) return null;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    @Test
    void insert() {
        var history = new History();
        history.setUser(userRepository.findById(2L).orElseThrow());
        history.setEpisode(episodeRep.findById(1L).orElseThrow());
        history.setPausedAt(toSeconds("15:23"));

        var h = service.insert(history);
        assertNotNull(h);
        assertNotNull(h.getId());

        var hr = rep.findById(h.getId());
        assertTrue(hr.isPresent(), "Histórico não encontrado no repositório");
        assertEquals("15:23", toTimeString(hr.get().getPausedAt()));

        service.delete(h.getId());
        assertFalse(rep.findById(h.getId()).isPresent(), "O histórico deveria ter sido excluído");
    }

    @Test
    void update() {
        // Arrange
        var history = new History();
        history.setPausedAt(toSeconds("19:24"));
        history.setEpisode(episodeRep.findById(1L).orElseThrow());
        history.setUser(userRepository.findById(2L).orElseThrow());

        var h = service.insert(history);
        assertNotNull(h);

        // Act
        h.setPausedAt(toSeconds("18:12"));
        var h2 = service.update(h);

        // Assert
        assertNotNull(h2);
        assertEquals("18:12", toTimeString(h2.getPausedAt()));

        service.delete(h2.getId());
        assertFalse(rep.findById(h2.getId()).isPresent(), "O histórico deveria ter sido excluído");
    }

    @Test
    void delete() {
        var history = new History();
        history.setUser(userRepository.findById(2L).orElseThrow());
        history.setEpisode(episodeRep.findById(1L).orElseThrow());
        history.setPausedAt(toSeconds("10:10"));

        var h = service.insert(history);
        assertNotNull(h.getId());

        service.delete(h.getId());
        assertFalse(rep.findById(h.getId()).isPresent(), "O histórico deveria ter sido excluído");
    }
}
