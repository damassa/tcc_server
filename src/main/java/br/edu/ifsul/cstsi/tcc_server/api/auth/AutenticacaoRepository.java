package br.edu.ifsul.cstsi.tcc_server.api.auth;

import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import org.springframework.data.repository.Repository;

/*
    Esta interce visa, única e exclusivamente, realizar a busca pelo usuário com UserDetails.
    Note que ela implementa Repository, ao invés de JpaRepository. Assim, a AutenticacaoRepository
    vem vazia, sem métodos CRUD, como a JpaRepository. Logo, essa interface só terá o(s) método(s)
    implementado (s)nela.
    A responsabilidade por CRUD de usuários fica a cargo da UsuarioRepository.
 */
public interface AutenticacaoRepository extends Repository<User,Long> {
    User findByEmail(String email);
}
