package br.edu.ifsul.cstsi.tcc_server.api.ratings;

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
public class RatingKey implements Serializable {
    @Column(name = "serie_id")
    Long serieID;
    @Column(name = "user_id")
    Long userID;
}
