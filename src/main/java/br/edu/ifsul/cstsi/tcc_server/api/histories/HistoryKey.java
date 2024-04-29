package br.edu.ifsul.cstsi.tcc_server.api.histories;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class HistoryKey implements Serializable {
    @Column(name = "episode_id")
    Long episodeID;

    @Column(name = "user_id")
    Long userID;
}
