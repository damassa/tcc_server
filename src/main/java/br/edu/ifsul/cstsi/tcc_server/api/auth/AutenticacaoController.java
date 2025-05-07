package br.edu.ifsul.cstsi.tcc_server.api.auth;

import br.edu.ifsul.cstsi.tcc_server.api.infra.security.TokenJwtDTO;
import br.edu.ifsul.cstsi.tcc_server.api.infra.security.TokenService;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import br.edu.ifsul.cstsi.tcc_server.api.users.validations.ValidationUserLogin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("api/v1/login") //Endpoint padrão da classe
@CrossOrigin(origins = "http://localhost:3000")
public class AutenticacaoController {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private AuthenticationManager manager; //o gerenciador de autenticação é quem dispara o loadUserByUsername (isto é, é interno do Spring Security, tem que usar ele)

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private TokenService tokenService;

    @Autowired
    private List<ValidationUserLogin> validations;

    @PostMapping
    public ResponseEntity<TokenJwtDTO> efetuaLogin(@RequestBody @Valid UsuarioAuthDTO data){
        var authenticationDTO = new UsernamePasswordAuthenticationToken(data.email(), data.senha()); //converte o DTO em DTO do Spring Security

        validations.forEach(v -> v.validate(data));

        var authentication = manager.authenticate(authenticationDTO); //autentica o usuário (esse objeto contém TUDO)
        var tokenJWT = tokenService.geraToken((User) authentication.getPrincipal()); //gera o token JWT para enviar na response
        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT)); //envia a response com o token JWT
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
