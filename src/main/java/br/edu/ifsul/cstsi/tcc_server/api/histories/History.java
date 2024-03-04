package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.List;

@Entity
public class History { //Intuito da tabela é pegar o tempo do episódio assistido por um usuário
    @EmbeddedId
    HistoryKey id;

    @ManyToMany
    @MapsId("episodeID")
    @JoinColumn(name = "episode_id")
    List<Episode> episodes;

    @ManyToMany
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    List<User> users;

    private Time pausedAt;
}
