package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RatingStatsDTO {
    private Double average;
    private Long totalVotes;

    // opcional: construtor vazio correto
    public RatingStatsDTO() {
        this.average = 0.0;
        this.totalVotes = 0L;
    }
}
