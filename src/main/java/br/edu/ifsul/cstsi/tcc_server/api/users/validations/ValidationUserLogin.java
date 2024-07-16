package br.edu.ifsul.cstsi.tcc_server.api.users.validations;

import br.edu.ifsul.cstsi.tcc_server.api.auth.UsuarioDTO;

public interface ValidationUserLogin {
    void validate(UsuarioDTO usuarioDTO);
}
