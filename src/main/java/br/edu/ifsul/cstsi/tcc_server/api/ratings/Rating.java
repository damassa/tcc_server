package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;


@Entity(name = "rating")
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"serie", "user"})
public class Rating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "serie_id")
    Serie serie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    private String comment;
    private int stars;

    public static Rating create(RatingDTOResponse r) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(r, Rating.class);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", serie=" + serie +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", stars=" + stars +
                '}';
    }
}
