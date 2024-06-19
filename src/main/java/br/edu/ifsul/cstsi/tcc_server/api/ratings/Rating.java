package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "rating")
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"serie", "user"})
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
