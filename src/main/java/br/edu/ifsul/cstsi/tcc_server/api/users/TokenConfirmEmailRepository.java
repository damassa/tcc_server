package br.edu.ifsul.cstsi.tcc_server.api.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenConfirmEmailRepository extends JpaRepository<TokenConfirmEmail, Long> {
    TokenConfirmEmail findByToken(String token);
}
