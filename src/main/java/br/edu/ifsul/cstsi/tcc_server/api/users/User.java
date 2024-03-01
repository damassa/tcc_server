package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.ratings.Rating;
import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "User")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @ManyToMany(mappedBy = "usersFavoriter")
    private List<Serie> favorites; //Nenhum ou mais usuários favoritam nenhuma ou várias séries

    @OneToMany(mappedBy = "user")
    private List<Serie> insertedSeries; //1 usuário cadastra várias séries

    //@ManyToMany
    private List<Episode> episodes; //1 ou mais usuários assistem 1 ou mais episódios

    private List<Rating> rating; //1 ou mais usuários avaliam 1 ou mais séries
}
