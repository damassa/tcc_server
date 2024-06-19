package br.edu.ifsul.cstsi.tcc_server.api.episodes;

import br.edu.ifsul.cstsi.tcc_server.api.histories.History;
import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity(name = "Episode")
@Table(name = "episodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"serie"})
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    private Serie serie; //Vários episódios são de apenas 1 série

    @OneToMany(mappedBy = "episode", fetch = FetchType.EAGER)
    private List<History> histories;
}
