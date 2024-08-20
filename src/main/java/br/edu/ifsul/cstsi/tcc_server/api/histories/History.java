package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;

@Entity(name = "history")
@Table(name = "histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"episode"})
public class History { //Intuito da tabela é pegar o tempo do episódio assistido por um usuário
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "episode_id")
    Episode episode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    private LocalTime pausedAt;

    public static History create(HistoryDTOResponse h) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(h, History.class);
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", episode=" + episode +
                ", user=" + user +
                ", pausedAt=" + pausedAt +
                '}';
    }
}
