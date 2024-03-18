package br.edu.ifsul.cstsi.tcc_server.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository rep;

    public List<User> getUsers() {
        return rep.findAll();
    }
}
