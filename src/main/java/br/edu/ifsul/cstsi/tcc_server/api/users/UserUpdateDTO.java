package br.edu.ifsul.cstsi.tcc_server.api.users;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    private String name;
    @Size(min = 3, message = "Senha deve ter no mínimo 3 caracteres")
    private String newPassword;
}
