package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.api.categories.Category;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.ratings.Rating;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;

import java.util.List;
@Entity(name = "Serie")
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String plot;
    private int year;
    private String image;
    private String bigImage;
    private String opening_video;
    private int duration;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "serie_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersFavoriter;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user; //Várias séries são cadastradas por 1 usuário admin

    @ManyToOne
    @JoinColumn(name = "categories_id", referencedColumnName = "id")
    private Category category; //Nenhuma ou várias séries possuem apenas 1 categoria

    @OneToMany(mappedBy = "serie")
    private List<Episode> episodes; //1 série possui vários episódios

    @OneToMany(mappedBy = "serie")
    List<Rating> ratings;
}
