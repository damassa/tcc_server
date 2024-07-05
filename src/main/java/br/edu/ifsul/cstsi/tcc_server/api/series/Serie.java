package br.edu.ifsul.cstsi.tcc_server.api.series;

import br.edu.ifsul.cstsi.tcc_server.api.categories.Category;
import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.ratings.Rating;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.modelmapper.ModelMapper; //TODO: Revisar com o professor se realmente precisa da dependência

import java.util.List;
@Entity(name = "Serie")
@Table(name = "series")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"user"})
public class Serie { // TODO: Criar DTO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String plot;
    private int year;
    private String image;
    private String bigImage;
    private String opening_video;

    @ManyToOne // NÃO QUERO BUSCAR ISSO
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user; //Várias séries são cadastradas por 1 usuário admin

    @ManyToOne(fetch = FetchType.EAGER) // BUSCAR ISSO
    @JoinColumn(name = "categories_id", referencedColumnName = "id")
    private Category category; //Nenhuma ou várias séries possuem apenas 1 categoria

    @OneToMany(mappedBy = "serie", fetch = FetchType.EAGER) // BUSCAR ISSO
    private List<Episode> episodes; //1 série possui vários episódios

    @OneToMany(mappedBy = "serie", fetch = FetchType.EAGER) // BUSCAR ISSO
    List<Rating> ratings;

    public Serie(String name, String plot, Integer year, String image, String bigImage, String opening_video) {
        this.name = name;
        this.plot = plot;
        this.year = year;
        this.image = image;
        this.bigImage = bigImage;
        this.opening_video = opening_video;
    }

    public static Serie create(Serie s) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(s, Serie.class);
    }
}
