package br.edu.ifsul.cstsi.tcc_server;

import br.edu.ifsul.cstsi.tcc_server.api.infra.security.TokenService;
import br.edu.ifsul.cstsi.tcc_server.api.users.AutenticacaoService;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(classes = TccServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseAPIIntegrationTest {
    @Autowired
    protected TestRestTemplate rest;
    @Autowired
    private AutenticacaoService service;
    @Autowired
    private TokenService tokenService;

    private String jwtToken = "";

    @BeforeEach
    public void setupTest() {
        User user = (User) service.loadUserByUsername("admin@admin.com");
        assertNotNull(user);

        jwtToken = tokenService.geraToken(user);
        assertNotNull(jwtToken);
    }

    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    protected <T> ResponseEntity<T> post(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, POST, new HttpEntity<>(body, headers), responseType);
    }

    protected <T> ResponseEntity<T> put(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, PUT, new HttpEntity<>(body, headers), responseType);
    }

    protected <T> ResponseEntity<T> get(String url, Class<T> responseType) {

        HttpHeaders headers = getHeaders();

        return rest.exchange(url, GET, new HttpEntity<>(headers), responseType);
    }

    protected <T> ResponseEntity<T> delete(String url, Class<T> responseType) {

        HttpHeaders headers = getHeaders();

        return rest.exchange(url, DELETE, new HttpEntity<>(headers), responseType);
    }
}
