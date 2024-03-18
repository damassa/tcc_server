package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "rating")
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @EmbeddedId
    RatingKey id;

    @ManyToOne
    @MapsId("serieID")
    @JoinColumn(name = "serie_id")
    Serie serie;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    User user;
    private String comment;
    private int stars;
}
