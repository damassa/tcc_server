package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Rating {
    @EmbeddedId
    RatingKey id;

    @ManyToMany
    @MapsId("serieID")
    @JoinColumn(name = "serie_id", referencedColumnName = "id")
    List<Serie> series;

    @ManyToMany
    @MapsId("userID")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    List<User> users;
    private String comment;
}
