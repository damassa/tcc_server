package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String comment;

    @ManyToMany
    private List<User> users; //1 ou mais usuários avaliam uma ou mais séries (TÔ EM DÚVIDA AQUI)

    @ManyToMany
    private List<Serie> series; //1 ou mais séries são avaliadas por 1 ou mais usuários (TÔ EM DÚVIDA AQUI)
}
