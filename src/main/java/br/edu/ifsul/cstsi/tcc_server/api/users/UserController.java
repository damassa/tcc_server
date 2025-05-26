package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.series.SerieDTOResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/*
    ### Algumas palavras sobre senhas (ao cadastrar um usuário você deve se preocupar com isso, questão de segurança)
    Vá no banco de dados e observe o hash gerada pelo BCryptPasswordEncoder, note que ele se divide em 4 partes:
        $2a$10$W8kA26PmcoqUZx4nle3h9uSGksu1bLmYLUF4H10jb.dlj2FEdLCCa (para senha 123)
    separadas pelos caracteres "$" e "." . Onde:
    $2a -> representa o algoritmo utilizado para encriptação e sua versão.
    $10 -> fator de trabalho (ou força de trabalho, ou rounds)
    $W8kA26PmcoqUZx4nle3h9uSGksu1bLmYLUF4H10jb.dlj2FEdLCCa -> salt + Checksum, um número aleatório fortemente criptografado que deve produzir resultados não determinísticos.

    *Salt: Em vez de usar apenas a senha como entrada para a função hash, bytes aleatórios (conhecidos como salt) são
    gerados para a senha de cada usuário.

    ## Detalhes sobre desempenho: Qual méthodo de validação de senha utilizar?
    Como as funções unidirecionais adaptativas (que gera uma hash para salvar no banco de dados) consomem,
    intencionalmente, muitos recursos, a validação de um nome de usuário e uma senha para cada solicitação pode
    degradar significativamente o desempenho de um aplicativo. Não há nada que o Spring Security (ou qualquer outra
    biblioteca) possa fazer para acelerar a validação da senha, uma vez que a segurança é obtida ao tornar o recurso
    de validação intensivo. Nesse sentido, os desenvolvedores são incentivados a trocar as credenciais de longo prazo
    (ou seja, que exigem o usuário e a senha para processar cada requisição, como, HTTPBasic) por uma credencial de
    curto prazo (como um Token JWT ou um Token OAuth, e assim por diante). A credencial de curto prazo pode ser
    validada rapidamente sem qualquer perda de segurança (desde que o token seja mantido em segredo, é claro :-) ).

    ## O 10 é a força de trabalho em new BCryptPasswordEncoder(10): Quanto maior o parâmetro de força, mais trabalho
    terá que ser feito (exponencialmente) para fazer o hash das senhas. O valor padrão é 10. Esperimente colocar,
    por exemplo, 16, e verá que demora mais a validação da senha.

    Para mais detalhes sobre senhas, consulte:
    https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
    e
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html

 */

@RestController
@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;
    private final RoleRepository roleRepository;

    public UserController(UserService service, RoleRepository roleRepository) {
        this.service = service;
        this.roleRepository = roleRepository;
    }

    @PostMapping(path = "/api/v1/users/register")
    @Transactional
    public ResponseEntity<String> register(@Valid @RequestBody UsuarioDTO usuarioDTO, UriComponentsBuilder uriComponentsBuilder) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        var user = new User();
        user.setName(usuarioDTO.name());
        user.setEmail(usuarioDTO.email());
        user.setPassword(encoder.encode(usuarioDTO.senha()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        try {
            var u = service.insert(user);
            var location = uriComponentsBuilder.path("/api/v1/users/register/{id}").buildAndExpand(u.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }
    }

    @GetMapping(path = "/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String confirmationToken) {
        try {
            boolean isConfirmed = service.confirmEmail(confirmationToken);
            if (isConfirmed) {
                return ResponseEntity.ok("E-mail confirmado com sucesso!");
            }
            return ResponseEntity.badRequest().body("Token inválido ou expirado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao confirmar e-mail: " + e.getMessage());
        }
    }

    @GetMapping(value = "/api/v1/users/{id}/favorites")
    public ResponseEntity<List<SerieDTOResponse>> getFavoritesByUserId(@PathVariable Long id) {
        var fav = service.getFavoriteSeriesById(id);
        System.out.println(fav);
        if (fav == null || fav.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fav.stream().map(SerieDTOResponse::new).collect(Collectors.toList()));

    }

    @PatchMapping(value = "/api/v1/users/edit-user")
    public ResponseEntity<String> editUser(@Valid @RequestBody UserUpdateDTO uDto, @AuthenticationPrincipal User user) {
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado!");
            }

            service.updateUser(user.getId(), uDto);
            return ResponseEntity.ok("Dados atualizados com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar dados: " + e.getMessage());
        }
    }


    @PostMapping("/api/v1/users/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        service.requestPasswordReset(email);
        return ResponseEntity.ok("Se o e-mail existir em nossa base de dados, você receberá um link para redefinição de senha.");
    }

    @PostMapping("/api/v1/users/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        boolean result = service.resetPassword(token, newPassword);
        if (result) {
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        }
        return ResponseEntity.badRequest().body("Token inválido ou expirado");
    }


//    @GetMapping(value = "/api/v1/users/me/favorites")
//    public ResponseEntity<List<SerieDTOResponse>> getFavoritesFromCurrentUser() {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String email = auth.getName();
//        System.out.println(auth);
//        System.out.println("Email: " + email);
//        var currUser = service.getUserByEmail(email);
//        System.out.println("Quem é?" + currUser);
//
//        if (currUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//
//    }

    @GetMapping(value = "/api/v1/users/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User user) {
        if (user == null) {
            System.out.println("Nenhum usuário autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Imprimindo informações do usuário
        System.out.println("Usuário logado:");
        System.out.println("ID: " + user.getId());
        System.out.println("Nome: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Roles: " + user.getRoles());

        return ResponseEntity.ok(new UserResponseDTO(user));
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
//    @Autowired
//    private UserService service;
//
//    @GetMapping()
//    public ResponseEntity<List<User>> get() {
//        List<User> list = service.getUsers();
//        return ResponseEntity.ok(list);
//    }
//
//    @GetMapping("/info")
//    public User userInfo(@AuthenticationPrincipal User user) { //a anotação retorna o user logado
//
//        User userLogged = (User) JwtUtil.getUserDetails(); //outra forma de retornar o user logado (nesse projeto)
//
//        return user;
//    }
}