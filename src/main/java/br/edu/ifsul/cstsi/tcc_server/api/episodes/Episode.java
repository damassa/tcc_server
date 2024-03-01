package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity(name = "Episode")
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    private Serie serie; //Vários episódios são de apenas 1 série
}
