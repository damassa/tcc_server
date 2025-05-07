package br.edu.ifsul.cstsi.tcc_server.api.series;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
@Service
public class SerieService { // TODO: Não tem que botar DTO aqui?
    @Autowired
    private SerieRepository rep;

    public List<Serie> getAllSeries() {
        return rep.findAll();
    }

    public Page<Serie> getSeries(Pageable pagination) {
        return rep.findAll(pagination);
    }

    public Optional<Serie> getSerieById(Long id) {
        return  rep.findById(id);
    }

    public List<Serie> getSeriesByName(String name) {
        return rep.findByName("%"+name+"%");
    }

    public List<Serie> getSeriesOrderedByYear(boolean asc) {
        Sort sort = asc ? Sort.by("year").ascending() : Sort.by("year").descending();
        return rep.findAll(sort);
    }

    public List<Serie> getSeriesByCategoryId(Long id) {
        return rep.getSeriesByCategoryId(id);
    }

    public void toggleFavorite(Long id_serie, Long id_user) {
        rep.insertFavorite(id_serie, id_user);
    }
    
    public Integer getFavoritesBySerieId(Long id_serie, Long id_user) {
        return rep.countSeriesByIds(id_serie, id_user);
    }

    public void removeFromFavorites(Long id_serie, Long id_user) {
        rep.removeFromFavorites(id_serie, id_user);
    }

    public Serie insert(Serie serie) {
        Assert.isNull(serie.getId(),"Não foi possível inserir o registro");

        return rep.save(serie);
    }

    public Serie update(Serie serie, Long id) {
        Assert.notNull(id,"Não foi possível atualizar o registro");

        // Busca o produto no banco de dados
        Optional<Serie> optional = rep.findById(id);
        if(optional.isPresent()) {
            Serie db = optional.get();
            // Copia as propriedades
            db.setName(serie.getName());
            db.setPlot(serie.getPlot());
            db.setCategory(serie.getCategory());
            db.setBigImage(serie.getBigImage());
            db.setImage(serie.getImage());
            db.setEpisodes(serie.getEpisodes());
            db.setOpening_video(serie.getOpening_video());
            db.setRatings(serie.getRatings());
            db.setYear(serie.getYear());
            System.out.println("Serie id " + db.getId());

            return rep.save(db);
        } else {
            return null;
            //throw new RuntimeException("Não foi possível atualizar o registro");
        }
    }

    public boolean delete(Long id) {
        Optional<Serie> optional = rep.findById(id);
        if(optional.isPresent()) {
            rep.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
}
