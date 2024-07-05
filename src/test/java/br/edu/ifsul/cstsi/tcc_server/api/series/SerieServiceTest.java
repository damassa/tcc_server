package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
@ActiveProfiles("test")
public class SerieServiceTest {
    @Autowired
    private SerieService service;

    @Test
    @DisplayName("Busca as séries na base de dados, espera 5 séries")
    public void getSeriesEsperaUmaPaginaCom5Objetos() {//PASSOU
        // ARRANGE
        var pageable = PageRequest.of(0,5);

        // ACT
        var series = service.getSeries(pageable);

        // ASSERT
        assertEquals(5, series.getContent().size());
    }

    @Test
    void getSerieByIdEsperaASerieDeId1() {//PASSOU
        // ARRANGE + ACT
        var s = service.getSerieById(1L);

        // ASSERT
        assertNotNull(s);
        assertEquals("Himitsu Sentai Gorenger", s.get().getName());
        assertEquals("The first super sentai heroes.", s.get().getPlot());
        assertEquals(1975, s.get().getYear());
        assertEquals("https://image.tmdb.org/t/p/w600_and_h900_bestv2/og6g7Ei0HIjj3bdi7mmNdNK2W3v.jpg", s.get().getImage());
        assertEquals("asdasdasd", s.get().getBigImage());
        assertEquals("https://www.youtube.com/watch?v=gbuEg2v9Arg", s.get().getOpening_video());
        assertEquals(6, s.get().getEpisodes().size());
        assertEquals(0, s.get().getRatings().size());
    }

    @Test
    void getSeriesByNameEsperaUmObjetoPorNomePesquisado() {//PASSOU
        // ARRANGE + ACT + ASSERT
        assertEquals(1, service.getSeriesByName("Himitsu Sentai Gorenger").size());
        assertEquals(1, service.getSeriesByName("J.A.Q.K Dengekitai").size());
        assertEquals(1, service.getSeriesByName("Battle Fever J").size());
        assertEquals(1, service.getSeriesByName("Denshi Sentai Denjiman").size());
        assertEquals(1, service.getSeriesByName("Taiyo Sentai Sun Vulcan").size());
    }

    @Test
    void insertEsperaOObjetoInseridoEDeleta() { // PASSOU
        // ARRANGE
        var serie = new Serie();
        serie.setName("Série Teste");
        serie.setPlot("Trama de uma série teste.");
        serie.setBigImage("pasdkoapsd");
        serie.setImage("asdopopkawe");
        serie.setOpening_video("https://www.youtube.com/watch?v=H9K8-3PHZOU");
        serie.setYear(2000);

        // ACT
        var s = service.insert(serie);

        // ASSERT
        assertNotNull(s);
        Long id = s.getId();
        assertNotNull(id);
        s = service.getSerieById(id).get();
        assertNotNull(s);
        assertEquals("Série Teste", s.getName());
        assertEquals("Trama de uma série teste.", s.getPlot());
        assertEquals("pasdkoapsd", s.getBigImage());
        assertEquals("asdopopkawe", s.getImage());
        assertEquals("https://www.youtube.com/watch?v=H9K8-3PHZOU", s.getOpening_video());
        assertEquals(2000, s.getYear());

        service.delete(id);
        if(service.getSerieById(id).isPresent()) {
            fail("A série não foi excluída.");
        }
    }

    @Test
    void updateEsperaOObjetoAlteradoERetornaAoValorOriginal() {//PASSOU
        // ARRANGE
        var sOriginal = service.getSerieById(1L).get();
        var serieMock = new Serie();
        serieMock.setId(sOriginal.getId());
        serieMock.setName("Série Teste Lalala");
        serieMock.setPlot("Trama de uma série teste lalala.");
        serieMock.setBigImage("pasdkoapsdLALALA");
        serieMock.setImage("asdopopkaweLALALA");
        serieMock.setOpening_video("https://www.youtube.com/watch?v=H9K8-3PHZOU");
        serieMock.setYear(2000);

        //ACT
        var serieAlterada = service.update(serieMock, serieMock.getId());

        // ASSERT
        assertNotNull(serieAlterada);
        assertEquals("Série Teste Lalala", serieAlterada.getName());
        assertEquals("Trama de uma série teste lalala.", serieAlterada.getPlot());
        assertEquals("pasdkoapsdLALALA", serieAlterada.getBigImage());
        assertEquals("asdopopkaweLALALA", serieAlterada.getImage());
        assertEquals("https://www.youtube.com/watch?v=H9K8-3PHZOU", serieAlterada.getOpening_video());
        assertEquals(2000, serieAlterada.getYear());

        // Volta ao valor original
        var serieOriginal = service.update(sOriginal, sOriginal.getId());
        assertNotNull(serieOriginal);
    }

    @Test
    void deleteEsperaAExclusaoDeUmObjetoInserido() { //PASSOU
        var serie = new Serie();
        serie.setName("ASDASD");
        serie.setPlot("OIDFOIJFDSOI OSIDFJOSIDF");
        serie.setBigImage("ODIODOSDIFOJD");
        serie.setImage("AUIAAA");
        serie.setOpening_video("QWWW");
        serie.setYear(2024);

        var s = service.insert(serie);

        assertNotNull(s);

        Long id = s.getId();
        assertNotNull(id);
        s = service.getSerieById(id).get();
        assertNotNull(s);

        service.delete(id);
        if(service.getSerieById(id).isPresent()) {
            fail("A série não foi excluída.");
        }
    }
}