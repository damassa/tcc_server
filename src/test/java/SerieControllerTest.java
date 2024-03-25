import br.edu.ifsul.cstsi.tcc_server.TccServerApplication;
import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SerieControllerTest extends BaseAPITest {
    private ResponseEntity<Serie> getSerie(String url) {
        //TRAVEI KKKKKKKK
    }

    private ResponseEntity<List<Serie>> getSeries(String url) {
        HttpHeaders headers = getHeaders();
        return rest.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Serie>>() {
        });
    }

    @Test
    public void selectAll() {
        List<Serie> series = getSeries("/api/v1/series").getBody();
        assertNotNull(series);
        assertEquals(3, series.size());

        series = getSeries("/api/v1/series?page=0&size=3").getBody();
        assertNotNull(series);
        assertEquals(3, series.size());
    }

    @Test
    public void selectByName() {
        assertEquals(1, getSeries("/api/v1/series/name/Himitsu").getBody().size());
        assertEquals(1, getSeries("/api/v1/series/name/Gorenger").getBody().size());
        assertEquals(1, getSeries("/api/series/name/O").getBody().size());

        assertEquals(HttpStatus.NO_CONTENT, getSeries("/api/v1/series/name/xxx").getStatusCode());
    }

    @Test
    public void selectById() {
        assertNotNull(getSerie("/api/v1/series/1"));
        assertNotNull(getSerie("/api/v1/series/2"));

        assertEquals(HttpStatus.NOT_FOUND, getSerie("/api/v1/series/999").getStatusCode());
    }
}
