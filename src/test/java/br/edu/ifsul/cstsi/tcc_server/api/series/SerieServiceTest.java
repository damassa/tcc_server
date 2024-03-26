package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = TccServerApplication.class)
class SerieServiceTest {
    @Autowired
    private SerieService service;
    @Test
    void getSeries() {
        var series = service.getSeries();
        assertEquals(2, series.size());
    }

    @Test
    void getSerieById() {
        var serie = service.getSerieById(2L);
        assertNotNull(serie);
        assertEquals("J.A.Q.K Dengekitai", serie.get().getName());
        //TODO: Não esquecer de testar as junções!!!!!
    }

    @Test
    void getSeriesByName() {
        assertEquals(1, service.getSeriesByName("Himitsu").size());
        assertEquals(1, service.getSeriesByName("Dengekitai").size());
    }

    @Test
    void insert() { // Cria uma série
        var serie = new Serie();
        serie.setName("Série Teste");
        serie.setPlot("Trama de uma série teste.");
        serie.setBigImage("pasdkoapsd");
        serie.setImage("asdopopkawe");
        serie.setOpening_video("https://www.youtube.com/watch?v=H9K8-3PHZOU");
        serie.setYear(2000);

        var s = service.insert(serie);
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
    void update() {
    }

    @Test
    void delete() {
    }
}