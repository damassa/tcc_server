package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;

@Entity(name = "history")
@Table(name = "histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class History { //Intuito da tabela é pegar o tempo do episódio assistido por um usuário
    @EmbeddedId
    HistoryKey id;

    @ManyToOne
    @MapsId("episodeID")
    @JoinColumn(name = "episode_id")
    Episode episode;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    User user;

    private LocalTime pausedAt;
}
