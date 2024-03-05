package br.edu.ifsul.cstsi.tcc_server.api.histories;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class HistoryKey implements Serializable {
    @Column(name = "episode_id")
    Long episodeID;

    @Column(name = "user_id")
    Long userID;
}
