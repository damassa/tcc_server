package br.edu.ifsul.cstsi.tcc_server.api.histories;

import br.edu.ifsul.cstsi.tcc_server.api.episodes.Episode;
import br.edu.ifsul.cstsi.tcc_server.api.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;
import java.util.List;

@Entity
public class History { //Intuito da tabela é pegar o tempo do episódio assistido por um usuário
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Time pausedAt;

    private List<User> users; //1 ou mais usuários assistem 1 ou mais séries (AQUI EU TÔ EM DÚVIDA)

    private List<Episode> episodes; //1 ou mais episódios são assistidos por 1 ou mais usuários
}
