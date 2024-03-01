package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class RatingKey implements Serializable {
    @Column(name = "serie_id")
    Long serieID;
    @Column(name = "user_id")
    Long userID;
}
