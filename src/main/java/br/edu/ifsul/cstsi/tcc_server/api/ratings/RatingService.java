package br.edu.ifsul.cstsi.tcc_server.api.ratings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository rep;

    public Rating insert (Rating rating) {
        Assert.notNull(rating.getId(), "Não foi possível inserir o registro.");
        return rep.save(rating);
    }

    public Rating update(Rating rating) {
        Assert.notNull(rating.getId(), "Não foi possível atualizar o registro.");
        Optional<Rating> optional = rep.findRatingById(rating.getId());
        if(optional.isPresent()) {
            Rating db = optional.get();
            db.setComment(rating.getComment());
            db.setStars(rating.getStars());
            return rep.save(db);
        } else {
            return null;
        }
    }

    public boolean delete(RatingKey id) {
        Optional<Rating> optional = rep.findRatingById(id);
        if(optional.isPresent()) {
            rep.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }
}
