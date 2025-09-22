package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.SerieDTOPost;
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
        // ARRANGE
        var serie = serieService.insert(new SerieDTOPost(
            "Série Teste",
                "Descrição Teste",
                2021,
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/og6g7Ei0HIjj3bdi7mmNdNK2W3v.jpg",
                "https://image.tmdb.org/t/p/w600_and_h900_bestv2/og6g7Ei0HIjj3bdi7mmNdNK2W3v.jpg",
                "https://www.youtube.com/watch?v=gbuEg2v9Arg",
                1L
        ));
        var dto = new EpisodeDTOPost(
                "Episódio Teste",
                20,
                "http://link.com/ep",
                serie.getId()
        );

        // ACT
        var e = service.insert(dto);

        // ASSERT
        assertNotNull(e);
        Long id = e.id();
        assertNotNull(id);

        var ep = rep.findById(id).orElse(null);
        assertNotNull(ep);
        assertEquals("Episódio Teste", ep.getName());
        assertEquals(20, ep.getDuration());

        // CLEANUP
        service.delete(id);
        assertTrue(rep.findById(id).isEmpty(), "O episódio deveria ter sido excluído.");
    }

    @Test
    void update() {
        // ARRANGE
        var epOriginal = rep.findById(1L).orElseThrow(); // episódio real do banco
        var dto = new EpisodeDTOPut(
                "Teste",
                20,
                "http://link.com/alterado",    // link vem aqui
                epOriginal.getSerie().getId() // serieId vem por último
        );

        // ACT
        var episodeAltered = service.update(dto, epOriginal.getId());

        // ASSERT
        assertNotNull(episodeAltered);
        assertEquals("Teste", episodeAltered.name());
        assertEquals(20, episodeAltered.duration());

        // rollback (mantém banco consistente)
        var rollbackDto = new EpisodeDTOPut(
                epOriginal.getName(),
                epOriginal.getDuration(),
                epOriginal.getLink(),
                epOriginal.getSerie().getId()
        );
        service.update(rollbackDto, epOriginal.getId());
    }


    @Test
    void delete() {
        insert(); // já cobre insert + delete
    }
}
