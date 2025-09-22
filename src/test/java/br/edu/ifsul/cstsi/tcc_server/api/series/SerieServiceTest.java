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
        assertEquals("Himitsu Sentai Gorenger", s.name());
        assertEquals("The first super sentai heroes.", s.plot());
        assertEquals(1975, s.year());
        assertEquals("https://image.tmdb.org/t/p/w600_and_h900_bestv2/og6g7Ei0HIjj3bdi7mmNdNK2W3v.jpg", s.image());
        assertEquals("asdasdasd", s.bigImage());
        assertEquals("https://www.youtube.com/watch?v=gbuEg2v9Arg", s.opening_video());
        assertEquals(1, s.episodes().size());
//        assertEquals(2, s.ratings().size());
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
//        var serie = new Serie();
//        serie.setName("Série Teste");
//        serie.setPlot("Trama de uma série teste.");
//        serie.setBigImage("pasdkoapsd");
//        serie.setImage("asdopopkawe");
//        serie.setOpening_video("https://www.youtube.com/watch?v=H9K8-3PHZOU");
//        serie.setYear(2000);
        var dto = new SerieDTOPost(
                "Série Teste",
                "Trama de uma série teste",
                2000,
                "asdopopkawe",
                "pasdkoapsd",
                "https://www.youtube.com/watch?v=H9K8-3PHZOU",
                1L
        );

        // ACT
        var s = service.insert(dto);

        // ASSERT
        assertNotNull(s);
        Long id = s.getId();
        assertNotNull(id);
        var fetched = service.getSerieById(id);
        assertEquals(dto.name(), fetched.name());
        assertEquals(dto.plot(), fetched.plot());
        assertEquals(dto.year(), fetched.year());
        assertEquals(dto.image(), fetched.image());
        assertEquals(dto.bigImage(), fetched.bigImage());
        assertEquals(dto.opening_video(), fetched.opening_video());

        service.delete(id);
        if(service.getSerieById(id) == null) {
            fail("A série não foi excluída.");
        }
    }

    @Test
    void updateEsperaOObjetoAlteradoERetornaAoValorOriginal() {
        // ARRANGE
        var sOriginal = service.getSerieById(1L);

        // Cria DTO com novos valores
        var dtoAlteracao = new SerieDTOPut(
                "Série Teste Lalala",
                "Trama de uma série teste lalala.", // ponto final
                2000,
                "asdopopkaweLALALA",
                "pasdkoapsdLALALA",
                "https://www.youtube.com/watch?v=H9K8-3PHZOU",
                sOriginal.categoryId()
        );

        // ACT - Atualiza a série
        var serieAlterada = service.update(sOriginal.id(), dtoAlteracao);

        // ASSERT - Verifica que os campos foram alterados
        assertEquals(dtoAlteracao.name(), serieAlterada.name());
        assertEquals(dtoAlteracao.plot(), serieAlterada.plot());
        assertEquals(dtoAlteracao.year(), serieAlterada.year());
        assertEquals(dtoAlteracao.image(), serieAlterada.image());
        assertEquals(dtoAlteracao.bigImage(), serieAlterada.bigImage());
        assertEquals(dtoAlteracao.opening_video(), serieAlterada.opening_video());
        assertEquals(dtoAlteracao.categoryId(), serieAlterada.categoryId());

        // VOLTA AO VALOR ORIGINAL
        var dtoOriginal = new SerieDTOPut(
                sOriginal.name(),
                sOriginal.plot(),
                sOriginal.year(),
                sOriginal.image(),
                sOriginal.bigImage(),
                sOriginal.opening_video(),
                sOriginal.categoryId()
        );

        var serieRestaurada = service.update(sOriginal.id(), dtoOriginal);

        // ASSERT - Verifica que voltou ao original
        assertNotNull(serieRestaurada);
        assertEquals(sOriginal.name(), serieRestaurada.name());
        assertEquals(sOriginal.plot(), serieRestaurada.plot());
        assertEquals(sOriginal.year(), serieRestaurada.year());
        assertEquals(sOriginal.image(), serieRestaurada.image());
        assertEquals(sOriginal.bigImage(), serieRestaurada.bigImage());
        assertEquals(sOriginal.opening_video(), serieRestaurada.opening_video());
        assertEquals(sOriginal.categoryId(), serieRestaurada.categoryId());
    }


    @Test
    void deleteEsperaAExclusaoDeUmObjetoInserido() { //
        var dto = new SerieDTOPost(
                "Série Delete",
                "Plot Delete",
                2025,
                "imagem",
                "imagem grande",
                "video",
                1L
        );

//        var serie = new Serie();
//        serie.setName("ASDASD");
//        serie.setPlot("OIDFOIJFDSOI OSIDFJOSIDF");
//        serie.setBigImage("ODIODOSDIFOJD");
//        serie.setImage("AUIAAA");
//        serie.setOpening_video("QWWW");
//        serie.setYear(2024);

        var s = service.insert(dto);

        assertNotNull(s);

        Long id = s.getId();

        boolean deleted = service.delete(id);
        assertTrue(deleted);

        assertTrue(service.getSerieById(id) == null);
    }
}