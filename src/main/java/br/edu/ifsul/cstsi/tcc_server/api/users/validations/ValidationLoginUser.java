package br.edu.ifsul.cstsi.tcc_server.api.users.validations;

import br.edu.ifsul.cstsi.tcc_server.api.users.UsuarioDTO;

public interface ValidationLoginUser {
    void validate(UsuarioDTO usuarioDTO);
}
